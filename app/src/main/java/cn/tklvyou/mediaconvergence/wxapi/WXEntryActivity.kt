package cn.tklvyou.mediaconvergence.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import cn.tklvyou.mediaconvergence.common.Contacts
import cn.tklvyou.mediaconvergence.utils.InterfaceUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.util.*

class WXEntryActivity : Activity(), IWXAPIEventHandler {
    private var mWeixinAPI: IWXAPI? = null
    private val RETURN_MSG_TYPE_LOGIN = 1
    private val RETURN_MSG_TYPE_SHARE = 2

    private var timer:Timer? = null
    private var timerTask:TimerTask? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWeixinAPI = WXAPIFactory.createWXAPI(this, Contacts.WX_APPID, true)
        mWeixinAPI!!.handleIntent(this.intent, this)

        timer = Timer()
        timerTask = object :TimerTask(){
            override fun run() {
                runOnUiThread {
                    finish()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        mWeixinAPI!!.handleIntent(intent, this)//必须调用此句话
    }

    override fun onResume() {
        super.onResume()
        timer?.schedule(timerTask,1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }

    //微信发送的请求将回调到onReq方法
    override fun onReq(req: BaseReq) {
        LogUtils.d("onReq")
        this.finish()
    }

    //发送到微信请求的响应结果
    override fun onResp(resp: BaseResp) {
        LogUtils.d("onResp")
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                LogUtils.d("ERR_OK")
                if (resp is SendAuth.Resp) {
                    //发送成功
                    val sendResp = resp
                    val code = sendResp.code
                    InterfaceUtils.getInstance().onClick(code)
                } else if (resp is SendMessageToWX.Resp) {
                    InterfaceUtils.getInstance().onClick("")
                }
                this.finish()
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                if (resp.type == RETURN_MSG_TYPE_SHARE) {
                    ToastUtils.showShort("分享失败")
                } else {
                    ToastUtils.showShort("登录失败")
                }
                LogUtils.e("ERR_USER_CANCEL")
                this.finish()
            }

            BaseResp.ErrCode.ERR_AUTH_DENIED ->{

                LogUtils.e("ERR_AUTH_DENIED")
                this.finish()
            }

            else -> {
                ToastUtils.showShort("微信登录错误")
                LogUtils.d("微信登录错误" + " " + resp.errCode + resp.errStr)
                this.finish()
            }
        }//发送取消
        //发送被拒绝
        //发送返回

        this.finish()
    }

}
