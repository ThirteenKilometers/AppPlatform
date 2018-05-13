/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\Projects\\AppPlatform\\app\\src\\main\\aidl\\com\\yw\\platform\\net\\MsgHandle.aidl
 */
package com.yw.platform.net;
// Declare any non-default types here with import statements

public interface MsgHandle extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.yw.platform.net.MsgHandle
{
private static final java.lang.String DESCRIPTOR = "com.yw.platform.net.MsgHandle";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.yw.platform.net.MsgHandle interface,
 * generating a proxy if needed.
 */
public static com.yw.platform.net.MsgHandle asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.yw.platform.net.MsgHandle))) {
return ((com.yw.platform.net.MsgHandle)iin);
}
return new com.yw.platform.net.MsgHandle.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_handle:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.handle(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.yw.platform.net.MsgHandle
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public boolean handle(java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_handle, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_handle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public boolean handle(java.lang.String msg) throws android.os.RemoteException;
}
