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
package generic

import scala.collection.parallel.Combiner
import scala.collection.parallel.ParIterable
import scala.collection.parallel.ParMap

import scala.annotation.unchecked.uncheckedVariance

/** A template trait for collections having a companion.
 *
 *  @tparam A    the element type of the collection
 *  @tparam CC   the type constructor representing the collection class
 */
trait GenericParTemplate[+A, +CC[X] <: ParIterable[X]]
  extends GenericTraversableTemplate[A, CC]
    with HasNewCombiner[A, CC[A] @uncheckedVariance]
{
  def companion: GenericParCompanion[CC]

  protected[this] override def newBuilder = newCombiner

  protected[this] override def newCombiner = companion.newCombiner[A]

  override def genericBuilder[B]: Combiner[B, CC[B]] = genericCombiner[B]

  def genericCombiner[B]: Combiner[B, CC[B]] = {
    val cb = companion.newCombiner[B]
    cb
  }

}


trait GenericParMapTemplate[K, +V, +CC[X, Y] <: ParMap[X, Y]] extends GenericParTemplate[(K, V), ParIterable]
{
  protected[this] override def newCombiner: Combiner[(K, V @uncheckedVariance), CC[K, V @uncheckedVariance]] = {
    val cb = mapCompanion.newCombiner[K, V]
    cb
  }

  def mapCompanion: GenericParMapCompanion[CC]

  def genericMapCombiner[P, Q]: Combiner[(P, Q), CC[P, Q]] = {
    val cb = mapCompanion.newCombiner[P, Q]
    cb
  }
}

