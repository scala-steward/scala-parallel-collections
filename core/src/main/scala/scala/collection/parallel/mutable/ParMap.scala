/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc. dba Akka
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package scala
package collection
package parallel.mutable

import scala.collection.generic._
import scala.collection.parallel.Combiner

/** A template trait for mutable parallel maps.
 *
 *  $sideeffects
 *
 *  @tparam K    the key type of the map
 *  @tparam V    the value type of the map
 */
trait ParMap[K, V]
extends parallel.ParMap[K, V]
   with ParIterable[(K, V)]
   with GenericParMapTemplate[K, V, ParMap]
   with ParMapLike[K, V, ParMap, ParMap[K, V], mutable.Map[K, V]]
{
  override def knownSize: Int = -1

  protected[this] override def newCombiner: Combiner[(K, V), ParMap[K, V]] = ParMap.newCombiner[K, V]

  override def mapCompanion: GenericParMapCompanion[ParMap] = ParMap

  override def empty: ParMap[K, V] = new ParHashMap[K, V]

  def seq: scala.collection.mutable.Map[K, V]

  /** The same map with a given default function.
   *  Note: `get`, `contains`, `iterator`, `keys`, etc are not affected by `withDefault`.
   *
   *  Invoking transformer methods (e.g. `map`) will not preserve the default value.
   *
   *  @param d     the function mapping keys to values, used for non-present keys
   *  @return      a wrapper of the map with a default value
   */
  def withDefault(d: K => V): scala.collection.parallel.mutable.ParMap[K, V] = new ParMap.WithDefault[K, V](this, d)

  /** The same map with a given default value.
   *
   *  Invoking transformer methods (e.g. `map`) will not preserve the default value.
   *
   *  @param d     default value used for non-present keys
   *  @return      a wrapper of the map with a default value
   */
  def withDefaultValue(d: V): scala.collection.parallel.mutable.ParMap[K, V] = new ParMap.WithDefault[K, V](this, x => d)
}

object ParMap extends ParMapFactory[ParMap, scala.collection.mutable.Map] {
  def empty[K, V]: ParMap[K, V] = new ParHashMap[K, V]

  def newCombiner[K, V]: Combiner[(K, V), ParMap[K, V]] = ParHashMapCombiner.apply[K, V]

  implicit def canBuildFrom[FromK, FromV, K, V]: CanCombineFrom[ParMap[FromK, FromV], (K, V), ParMap[K, V]] = new CanCombineFromMap[FromK, FromV, K, V]

  class WithDefault[K, V](underlying: ParMap[K, V], d: K => V)
  extends scala.collection.parallel.ParMap.WithDefault(underlying, d) with ParMap[K, V] {
    override def knownSize = underlying.knownSize
    def addOne(kv: (K, V)) = {underlying += kv; this}
    def subtractOne(key: K) = {underlying -= key; this}
    override def empty = new WithDefault(underlying.empty, d)
    override def updated[U >: V](key: K, value: U): WithDefault[K, U] = new WithDefault[K, U](underlying.updated[U](key, value), d)
    override def + [U >: V](kv: (K, U)): WithDefault[K, U] = updated(kv._1, kv._2)
    override def - (key: K): WithDefault[K, V] = new WithDefault(underlying - key, d)
    override def seq = underlying.seq.withDefault(d)
    def clear() = underlying.clear()
    def put(key: K, value: V): Option[V] = underlying.put(key, value)

    /** If these methods aren't overridden to thread through the underlying map,
     *  successive calls to withDefault* have no effect.
     */
    override def withDefault(d: K => V): ParMap[K, V] = new WithDefault[K, V](underlying, d)
    override def withDefaultValue(d: V): ParMap[K, V] = new WithDefault[K, V](underlying, x => d)
  }
}
