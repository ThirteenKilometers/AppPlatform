/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\Projects\\AppPlatform\\app\\src\\main\\aidl\\com\\yw\\platform\\tools\\nettyn\\NetInterface.aidl
 */
package com.yw.platform.tools.nettyn;
// Declare any non-default types here with import statements

public interface NetInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.yw.platform.tools.nettyn.NetInterface
{
private static final java.lang.String DESCRIPTOR = "com.yw.platform.tools.nettyn.NetInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.yw.platform.tools.nettyn.NetInterface interface,
 * generating a proxy if needed.
 */
public static com.yw.platform.tools.nettyn.NetInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.yw.platform.tools.nettyn.NetInterface))) {
return ((com.yw.platform.tools.nettyn.NetInterface)iin);
}
return new com.yw.platform.tools.nettyn.NetInterface.Stub.Proxy(obj);
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
case TRANSACTION_startWork:
{
data.enforceInterface(DESCRIPTOR);
this.startWork();
reply.writeNoException();
return true;
}
case TRANSACTION_send:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.yw.platform.tools.nettyn.SendBack _arg1;
_arg1 = com.yw.platform.tools.nettyn.SendBack.Stub.asInterface(data.readStrongBinder());
this.send(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getSate:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getSate();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setMsgHandle:
{
data.enforceInterface(DESCRIPTOR);
com.yw.platform.net.MsgHandle _arg0;
_arg0 = com.yw.platform.net.MsgHandle.Stub.asInterface(data.readStrongBinder());
this.setMsgHandle(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.yw.platform.tools.nettyn.NetInterface
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
@Override public void startWork() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startWork, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void send(java.lang.String data, com.yw.platform.tools.nettyn.SendBack back) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(data);
_data.writeStrongBinder((((back!=null))?(back.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_send, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getSate() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSate, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setMsgHandle(com.yw.platform.net.MsgHandle msgHandle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((msgHandle!=null))?(msgHandle.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setMsgHandle, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_startWork = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_send = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getSate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_setMsgHandle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void startWork() throws android.os.RemoteException;
public void send(java.lang.String data, com.yw.platform.tools.nettyn.SendBack back) throws android.os.RemoteException;
public int getSate() throws android.os.RemoteException;
public void setMsgHandle(com.yw.platform.net.MsgHandle msgHandle) throws android.os.RemoteException;
}
