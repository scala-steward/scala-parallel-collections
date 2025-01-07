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

import mutable.Builder
import scala.annotation.unchecked.uncheckedVariance

trait HasNewBuilder[+A, +Repr] extends Any {
  /** The builder that builds instances of Repr */
  protected[this] def newBuilder: Builder[A @uncheckedVariance, Repr]
}
