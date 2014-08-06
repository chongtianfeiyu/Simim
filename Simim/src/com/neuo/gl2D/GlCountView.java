package com.neuo.gl2D;

import com.neuo.common.Count;
import com.neuo.common.UpdateManager.CommonCalcu;
import com.neuo.glcommon.GlView;
import com.neuo.globject.GlObject;
import com.neuo.glshader.GlShader;

public class GlCountView extends GlTextView
implements CommonCalcu {
	private Count count;
	public GlCountView(String name, GlView glView, GlShader glShader,
			GlObject glObject, int digit) {
		super(name, glView, glShader, glObject);
		this.count = new Count();
		setTextInterface(count);
	}
	
	public GlCountView(String name, GlView glView, GlShader glShader,
			GlObject glObject) {
		super(name, glView, glShader, glObject);
		this.count = new Count();
		setTextInterface(count);
	}

	@Override
	public void calcu(long interval) {
		this.count.calcu(interval);
	}
	
	public Count getCount() {
		return this.count;
	}
}
