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

trait CustomParallelizable[+A, +ParRepr <: Parallel] extends Any with Parallelizable[A, ParRepr] {
  override def par: ParRepr
  override protected[this] def parCombiner = throw new UnsupportedOperationException("")
}

