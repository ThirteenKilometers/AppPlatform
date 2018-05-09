// NetInterface.aidl
package com.yw.platform.tools.nettyn;

import com.yw.platform.tools.nettyn.SendBack;
import com.yw.platform.net.MsgHandle;
// Declare any non-default types here with import statements
interface NetInterface {
      void startWork();
      void send(String data,SendBack back);
      int  getSate();
      void setMsgHandle(MsgHandle msgHandle);
}
