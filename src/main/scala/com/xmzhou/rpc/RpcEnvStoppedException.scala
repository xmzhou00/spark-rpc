package com.xmzhou.rpc

private[rpc] class RpcEnvStoppedException()
  extends IllegalStateException("RpcEnv already stopped.")