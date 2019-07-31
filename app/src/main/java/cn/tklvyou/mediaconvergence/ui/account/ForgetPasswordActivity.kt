package cn.tklvyou.mediaconvergence.ui.account

import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.widget.TimeCount
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : BaseActivity<AccountForgetPresenter>(), AccountContract.ForgetView,View.OnClickListener {


    override fun getActivityLayoutID(): Int {
        return R.layout.activity_forget_password
    }

    override fun initPresenter(): AccountForgetPresenter {
        return AccountForgetPresenter()
    }

    override fun initView() {
        hideTitleBar()

        titleBar.setNavigationListener {
            finish()
        }

        etMobile.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    val account = p0.toString().trim()
                    if (account.length == 11 && account.substring(0, 1) == "1") {
                        ivRight.visibility = View.VISIBLE
                    } else {
                        ivRight.visibility = View.INVISIBLE
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        btnResetPassword.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        btnGetCaptcha.setOnClickListener(this)

    }

    override fun getCaptchaSuccess() {
        val timeCount = TimeCount(60000, 1000, object : TimeCount.ITimeCountListener {
            override fun onTick(millisUntilFinished: Long) {
                btnGetCaptcha.setTextColor(Color.parseColor("#999999"))
                btnGetCaptcha.isClickable = false
                btnGetCaptcha.text = "${millisUntilFinished / 1000}秒"
            }

            override fun onFinish() {
                btnGetCaptcha.setTextColor(resources.getColor(R.color.colorAccent))
                btnGetCaptcha.isClickable = true
                btnGetCaptcha.text = "发送验证码"
            }

        })
        timeCount.start()
    }

    override fun resetpwdSuccess() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }


    override fun onClick(p0: View?) {
        if (p0 == null) {
            return
        }
        when (p0.id) {
            R.id.btnBack -> {
                finish()
            }

            R.id.btnGetCaptcha -> {
                val mobile = etMobile.text.toString().trim()

                if (ivRight.visibility != View.VISIBLE) {
                    ToastUtils.showShort("请输入正确的手机号")
                    return
                }

                mPresenter.getCaptcha(mobile, "resetpwd")
            }

            R.id.btnResetPassword -> {
                val mobile = etMobile.text.toString().trim()
                val captcha = etCaptcha.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val confirmPassword = etConfirmPassword.text.toString().trim()


                if (ivRight.visibility != View.VISIBLE) {
                    ToastUtils.showShort("请输入正确的手机号")
                    return
                }

                if (captcha.isEmpty()) {
                    ToastUtils.showShort("请输入验证码")
                    return
                }

                if (password.isEmpty()) {
                    ToastUtils.showShort("请输入新密码")
                    return
                }

                if (confirmPassword.isEmpty()) {
                    ToastUtils.showShort("请输入确认新密码")
                    return
                }

                if (password != confirmPassword) {
                    ToastUtils.showShort("两次输入密码不一致")
                    return
                }

                mPresenter.resetpwd(mobile, password, captcha)

            }


        }

    }


}