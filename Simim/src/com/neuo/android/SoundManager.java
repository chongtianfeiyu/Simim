package com.neuo.android;

import java.util.ArrayList;

import com.neuo.common.WorkState;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseArray;
import android.app.Service;
import android.content.Context;

public class SoundManager {

	private SoundPool soundPool;
	private SparseArray<MediaInfo> mediaPlayerMap;
	private SparseArray<SoundIdInfo> soundIdMap;
	private ArrayList<Integer> soundIdIndexs;

	private int currMediaPlayId;
	private Context soundContext;
	private WorkState state;
	private boolean isOpenMusic;
	
	private static class MediaInfo {
		public MediaPlayer mediaPlayer;
		public boolean isPrepare;
		
		public MediaInfo() {
			mediaPlayer = null;
			isPrepare = false;
		}
	}
	
	private static class SoundIdInfo {
		public int sounldId;
		public boolean isLoad;
		
		public SoundIdInfo() {
			isLoad = false;
		}
	}
	
	private static final Integer soundPoolMaxSize = 5; 
	private static SoundManager soundManager;
	
	public static SoundManager getSoundManager(){
		return soundManager;
	}
	
	public static SoundManager initSoundManager(Context context, boolean isOpen){
		if (null == soundManager) {
			soundManager = new SoundManager();
			soundManager.initSound(context, isOpen);
		}
		return soundManager;
	}
	
	public static void release() {
		if (null != soundManager) {
			soundManager.uninitSound();
			soundManager = null;
		}
	}

	public void addStreamVolume() {
		AudioManager audioManager = (AudioManager)soundContext.getSystemService(Service.AUDIO_SERVICE);
		audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
												AudioManager.FLAG_SHOW_UI);
	}
	
	public void delStreamVolume() {
		AudioManager audioManager = (AudioManager)soundContext.getSystemService(Service.AUDIO_SERVICE);
		audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
												AudioManager.FLAG_SHOW_UI);
	}
	
	public void openMusic(boolean isOpen, boolean isContinue) {
		if (isOpen && !isOpenMusic) {
			isOpenMusic = true;
			if (isContinue && currMediaPlayId >= 0) {
				start(false);
			}
		} else if (!isOpen && isOpenMusic) {
			stop(true);
			isOpenMusic = false;
		}
	}
	
	
	public void start(int resId, boolean isRestart) {
		if (resId != -1) {
			if (currMediaPlayId != resId) {
				stop(false);
				if (!createMedia(resId)) {
					return;
				}
			}
			currMediaPlayId = resId;
			start(isRestart);
		}
	}
	
	public void playSoundPool(int resId) {
		if (!isOpenMusic) {
			return;
		}
		boolean canPlay = false;
		int si;
		synchronized (soundIdMap) {
			SoundIdInfo soundId = soundIdMap.get(resId);
			if (null == soundId) {
				return;
			} else {
				canPlay = soundId.isLoad;
				si = soundId.sounldId;
			}

		}
		if (!canPlay) {
			return;
		} else {
			soundPool.play(si, 1f, 1f, 0, 0, 1);
		}
	}

	public void stop(boolean isContinue) {
		if (state != WorkState.STOP) {
			MediaInfo tmpInfo = mediaPlayerMap.get(currMediaPlayId);
			tmpInfo.mediaPlayer.stop();
			tmpInfo.isPrepare = false;
			state = WorkState.STOP;
		}
		if (!isContinue) {
			currMediaPlayId = -1;
		}
	}

	private void start(boolean isRestart) {
		if (!isOpenMusic) {
			return;
		}
		if (currMediaPlayId < 0) {
			return;
		}
		MediaInfo tmpInfo = mediaPlayerMap.get(currMediaPlayId);
		if (WorkState.STOP == state) {
			if (tmpInfo.isPrepare) {
				tmpInfo.mediaPlayer.start();
			} else {
				try {
					tmpInfo.mediaPlayer.prepare();
					tmpInfo.isPrepare = true;
				} catch (Exception e) {
					tmpInfo.isPrepare = false;
					return;
				}
				if (isRestart) {
					tmpInfo.mediaPlayer.seekTo(0);
				}
				tmpInfo.mediaPlayer.start();
			}
			state = WorkState.RUN;
		} else if (WorkState.PAUSE == state) {
			tmpInfo.mediaPlayer.start();
			state = WorkState.RUN;
		} else if (isRestart) {
			tmpInfo.mediaPlayer.seekTo(0);
		}
	}
	
	private boolean createMedia(int resId) {
		MediaInfo tmp = mediaPlayerMap.get(resId);
		if (tmp == null) {
			tmp = new MediaInfo();
			tmp.mediaPlayer = MediaPlayer.create(soundContext, resId);
			if (tmp.mediaPlayer == null) {
				return false;
			} else {
				tmp.mediaPlayer.setLooping(true);
				tmp.isPrepare = true;
				mediaPlayerMap.put(resId, tmp);
				return true;
			}
			/*
			AssetFileDescriptor afd = soundContext.getResources().openRawResourceFd(resId);
			if (afd != null) {

				try {
					tmp.mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
				} catch (IOException e) {
					return false;
				}

				try {
					afd.close();
				} catch (IOException e) {
				}
				mediaPlayerMap.put(resId, tmp);
				return true;
			} else {
				return false;
			}
			*/
			
		} else {
			return true;
		}
	}
	
	public void pause() {
		if (state == WorkState.RUN) {
			mediaPlayerMap.get(currMediaPlayId).mediaPlayer.pause();
			state = WorkState.PAUSE;
		}
	}
	
	public void resume() {
		if (state == WorkState.PAUSE) {
			mediaPlayerMap.get(currMediaPlayId).mediaPlayer.start();
			state = WorkState.RUN;
		}
	}
	
	
	private void releaseAllMedia() {
		for (int i = 0; i < mediaPlayerMap.size(); i++) {
			mediaPlayerMap.valueAt(i).mediaPlayer.release();
		}
		mediaPlayerMap.clear();
	}
	
	private void uninitSound() {
		if (null != soundPool) {
			soundPool.release();
			soundPool = null;
		}
		if (null != mediaPlayerMap) {
			releaseAllMedia();
		}
	}
	
	public void loadSingleSoundPool(int resId) {
		synchronized (soundIdMap) {
			SoundIdInfo tmpIdInfo = soundIdMap.get(resId);
			if (tmpIdInfo != null) {
				return;
			} else {
				tmpIdInfo = new SoundIdInfo();
				soundIdMap.put(resId, tmpIdInfo);
				soundIdIndexs.add(resId);
				soundPool.load(soundContext, resId, 1);
			}
		}
	}
	
	private void initSound(Context context, boolean isOpen) {
		soundContext = context;
		soundPool = new SoundPool(soundPoolMaxSize, AudioManager.STREAM_MUSIC, 0);
		soundIdMap = new SparseArray<SoundIdInfo>();
		soundIdIndexs = new ArrayList<Integer>();
		soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				synchronized (soundIdMap) {
					if (soundIdIndexs.size() > 0) {
						SoundIdInfo tmpIdInfo = soundIdMap.get(soundIdIndexs.get(0));
						tmpIdInfo.sounldId = sampleId;
						tmpIdInfo.isLoad = (status == 0);
						soundIdIndexs.remove(0);
					}
				}
			}
		});

		mediaPlayerMap = new SparseArray<MediaInfo>();
		state = WorkState.STOP;
		currMediaPlayId = -1;
		this.isOpenMusic = isOpen;
	}
	
	private SoundManager() {
	}
}
