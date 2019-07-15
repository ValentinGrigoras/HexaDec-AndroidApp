package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.presenter.contract.MainContract

class SettingPresenter(view: MainContract.SettingView): MainContract.SettingContract {
    private var view: MainContract.SettingView? = view

    override fun updatePin(newPin:String) {
        if(HexaDec.user.getPin() != newPin) {
            HexaDec.user.setPin(newPin)
            Thread {
                HexadecUserConnection.putOperation(HexadecUserConnection.convertToJSON(HexaDec.user))
            }.start()
        }
    }

    override fun onDestroy() {
        this.view=null
    }

}