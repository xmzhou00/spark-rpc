package com.xmzhou.rpc

import com.xmzhou.RpcConf
import com.xmzhou.rpc.netty.NettyRpcEnvFactory
import org.slf4j.LoggerFactory

/**
 * Master
 *
 * @param rpcEnv
 */
class Master(
              override val rpcEnv: RpcEnv,
              address: RpcAddress,
            ) extends RpcEndpoint {

  private val logger = LoggerFactory.getLogger(classOf[Master])


  /**
   * Invoked before [[RpcEndpoint]] starts to handle any message.
   */
  override def onStart(): Unit = {
    logger.info("master started ...")
  }


  /**
   * Process messages from `RpcEndpointRef.ask`. If receiving a unmatched message,
   * `RpcException` will be thrown and sent to `onError`.
   */
  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case BoundPortsRequest => {
      logger.info("接收到请求")
      context.reply(BoundPortsResponse(address.port))
    }

  }

  /**
   * Invoked when [[RpcEndpoint]] is stopping. `self` will be `null` in this method and you cannot
   * use it to send or ask messages.
   */
  override def onStop(): Unit = {
    logger.info("stop Server endpoint")
  }

}

object Master {
  def main(args: Array[String]): Unit = {
    // 封装配置信息
    val rpcEnvConfig = RpcEnvConfig(RpcConf(false), "test-server", "localhost", 9999)
    //  RpcEnv  -> ActorSystem
    val rpcEnv = new NettyRpcEnvFactory().create(config = rpcEnvConfig)

    val masterRef = rpcEnv.setupEndpoint("Master", new Master(rpcEnv, rpcEnv.address))


    val response = masterRef.askSync[BoundPortsResponse](BoundPortsRequest)
    println(response)

    rpcEnv.awaitTermination()
  }
}
