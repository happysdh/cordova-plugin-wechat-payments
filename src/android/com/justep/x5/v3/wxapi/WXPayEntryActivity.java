package com.xunao.udsa.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.justep.cordova.Config;
import com.justep.cordova.plugin.weixin.WeixinV3;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

	private static final String TAG = "Weixin";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.init();
    	api = WXAPIFactory.createWXAPI(this,Config.getPreferences().getString("weixinappid", ""));
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

			if(WeixinV3.currentCallbackContext != null) {
				switch (resp.errCode) {
					case 0:{
						WeixinV3.currentCallbackContext.success();
						break;
					}
					case -1:
					case -2:
					case -3:
					case -4:
					case -5:{
						WeixinV3.currentCallbackContext.error(resp.errCode);
						break;
					}
				}
			}
			finish();

//			Intent intent;
//			try {
//				intent = new Intent(this, WXPayEntryActivity.class.getClassLoader().loadClass(Constants.PACKNAME+ "."+ Constants.ACTIVITYCLASSNAME));
//				Bundle bundle=new Bundle();
//				bundle.putInt("weixinPayRespCode",  resp.errCode);
//				bundle.putString("intentType", "com.justep.cordova.plugin.weixin.WeixinV3");
//			    intent.putExtras(bundle);
//			    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			    Log.i(TAG, "startActivity");
//			    startActivity(intent);
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
		}
	}
}
