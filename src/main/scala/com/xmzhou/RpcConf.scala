package com.xmzhou

import com.xmzhou.util.Utils

import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConverters.asScalaSetConverter

case class RpcConf(loadDefaults: Boolean) extends Cloneable with Serializable {


  /** Create a SparkConf that loads defaults from system properties and the classpath */
  def this() = this(true)

  private val settings = new ConcurrentHashMap[String, String]()


  /**
   * 加载配置
   */
  if (loadDefaults) {
    loadFromSystemProperties(false)
  }

  def loadFromSystemProperties(silent: Boolean): RpcConf = {
    // Load any spark.* system properties
    for ((key, value) <- Utils.getSystemProperties if key.startsWith("spark.")) {
      set(key, value)
    }
    this
  }

  /** Set a configuration variable. */
  def set(key: String, value: String): RpcConf = {
    settings.put(key, value)
    this
  }


  /** Remove a parameter from the configuration */
  def remove(key: String): RpcConf = {
    settings.remove(key)
    this
  }

  /** Does the configuration contain a given parameter? */
  def contains(key: String): Boolean = {
    settings.containsKey(key)
  }


  /** Get a parameter; throws a NoSuchElementException if it's not set */
  def get(key: String): String = {
    getOption(key).getOrElse(throw new NoSuchElementException(key))
  }

  /** Get a parameter, falling back to a default if not set */
  def get(key: String, defaultValue: String): String = {
    getOption(key).getOrElse(defaultValue)
  }


  def getOption(str: String): Option[String] = {
    Option(settings.get(str))
  }


  /**
   * Get a size parameter as bytes; throws a NoSuchElementException if it's not set. If no
   * suffix is provided then bytes are assumed.
   *
   * @throws java.util.NoSuchElementException If the size parameter is not set
   * @throws NumberFormatException            If the value cannot be interpreted as bytes
   */
  def getSizeAsBytes(key: String): Long = {
    Utils.byteStringAsBytes(get(key))
  }

  /**
   * Get a size parameter as bytes, falling back to a default if not set. If no
   * suffix is provided then bytes are assumed.
   *
   * @throws NumberFormatException If the value cannot be interpreted as bytes
   */
  def getSizeAsBytes(key: String, defaultValue: String): Long = {
    Utils.byteStringAsBytes(get(key, defaultValue))
  }

  /**
   * Get a size parameter as bytes, falling back to a default if not set.
   *
   * @throws NumberFormatException If the value cannot be interpreted as bytes
   */
  def getSizeAsBytes(key: String, defaultValue: Long): Long = {
    Utils.byteStringAsBytes(get(key, defaultValue + "B"))
  }

  /**
   * Get a size parameter as Kibibytes; throws a NoSuchElementException if it's not set. If no
   * suffix is provided then Kibibytes are assumed.
   *
   * @throws java.util.NoSuchElementException If the size parameter is not set
   * @throws NumberFormatException            If the value cannot be interpreted as Kibibytes
   */
  def getSizeAsKb(key: String): Long = {
    Utils.byteStringAsKb(get(key))
  }

  /**
   * Get a size parameter as Kibibytes, falling back to a default if not set. If no
   * suffix is provided then Kibibytes are assumed.
   *
   * @throws NumberFormatException If the value cannot be interpreted as Kibibytes
   */
  def getSizeAsKb(key: String, defaultValue: String): Long = {
    Utils.byteStringAsKb(get(key, defaultValue))
  }

  /**
   * Get a size parameter as Mebibytes; throws a NoSuchElementException if it's not set. If no
   * suffix is provided then Mebibytes are assumed.
   *
   * @throws java.util.NoSuchElementException If the size parameter is not set
   * @throws NumberFormatException            If the value cannot be interpreted as Mebibytes
   */
  def getSizeAsMb(key: String): Long = {
    Utils.byteStringAsMb(get(key))
  }

  /**
   * Get a size parameter as Mebibytes, falling back to a default if not set. If no
   * suffix is provided then Mebibytes are assumed.
   *
   * @throws NumberFormatException If the value cannot be interpreted as Mebibytes
   */
  def getSizeAsMb(key: String, defaultValue: String): Long = {
    Utils.byteStringAsMb(get(key, defaultValue))
  }

  /**
   * Get a size parameter as Gibibytes; throws a NoSuchElementException if it's not set. If no
   * suffix is provided then Gibibytes are assumed.
   *
   * @throws java.util.NoSuchElementException If the size parameter is not set
   * @throws NumberFormatException            If the value cannot be interpreted as Gibibytes
   */
  def getSizeAsGb(key: String): Long = {
    Utils.byteStringAsGb(get(key))
  }

  /**
   * Get a size parameter as Gibibytes, falling back to a default if not set. If no
   * suffix is provided then Gibibytes are assumed.
   *
   * @throws NumberFormatException If the value cannot be interpreted as Gibibytes
   */
  def getSizeAsGb(key: String, defaultValue: String): Long = {
    Utils.byteStringAsGb(get(key, defaultValue))
  }


  /** Get all parameters as a list of pairs */
  def getAll: Array[(String, String)] = {
    settings.entrySet().asScala.map(x => (x.getKey, x.getValue)).toArray
  }

  /**
   * Get all parameters that start with `prefix`
   */
  def getAllWithPrefix(prefix: String): Array[(String, String)] = {
    getAll.filter {
      case (k, v) => k.startsWith(prefix)
    }
      .map {
        case (k, v) => (k.substring(prefix.length), v)
      }
  }


  /**
   * Get a parameter as an integer, falling back to a default if not set
   *
   * @throws NumberFormatException If the value cannot be interpreted as an integer
   */
  def getInt(key: String, defaultValue: Int): Int = {
    getOption(key).map(_.toInt).getOrElse(defaultValue)
  }

  /**
   * Get a parameter as a long, falling back to a default if not set
   *
   * @throws NumberFormatException If the value cannot be interpreted as a long
   */
  def getLong(key: String, defaultValue: Long): Long = {
    getOption(key).map(_.toLong).getOrElse(defaultValue)
  }

  /**
   * Get a parameter as a double, falling back to a default if not ste
   *
   * @throws NumberFormatException If the value cannot be interpreted as a double
   */
  def getDouble(key: String, defaultValue: Double): Double = {
    getOption(key).map(_.toDouble).getOrElse(defaultValue)
  }

  /**
   * Get a parameter as a boolean, falling back to a default if not set
   *
   * @throws IllegalArgumentException If the value cannot be interpreted as a boolean
   */
  def getBoolean(key: String, defaultValue: Boolean): Boolean = {
    getOption(key).map(_.toBoolean).getOrElse(defaultValue)
  }


  /**
   * Get a time parameter as milliseconds; throws a NoSuchElementException if it's not set. If no
   * suffix is provided then milliseconds are assumed.
   *
   * @throws java.util.NoSuchElementException If the time parameter is not set
   * @throws NumberFormatException            If the value cannot be interpreted as milliseconds
   */
  def getTimeAsMs(key: String): Long = {
    Utils.timeStringAsMs(get(key))
  }

  /**
   * Get a time parameter as milliseconds, falling back to a default if not set. If no
   * suffix is provided then milliseconds are assumed.
   *
   * @throws NumberFormatException If the value cannot be interpreted as milliseconds
   */
  def getTimeAsMs(key: String, defaultValue: String): Long = {
    Utils.timeStringAsMs(get(key, defaultValue))
  }

  /**
   * Get a time parameter as seconds; throws a NoSuchElementException if it's not set. If no
   * suffix is provided then seconds are assumed.
   *
   * @throws java.util.NoSuchElementException If the time parameter is not set
   * @throws NumberFormatException            If the value cannot be interpreted as seconds
   */
  def getTimeAsSeconds(key: String): Long = {
    Utils.timeStringAsSeconds(get(key))
  }

  /**
   * Get a time parameter as seconds, falling back to a default if not set. If no
   * suffix is provided then seconds are assumed.
   *
   * @throws NumberFormatException If the value cannot be interpreted as seconds
   */
  def getTimeAsSeconds(key: String, defaultValue: String): Long = {
    Utils.timeStringAsSeconds(get(key, defaultValue))
  }

  /** Set a parameter if it isn't already configured */
  def setIfMissing(key: String, value: String): RpcConf = {
    settings.putIfAbsent(key, value)
    this
  }

  override def toString: String = settings.toString

}
