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

package scala.collection.parallel

trait Operators[T] {
  def reduceOperators: List[(T, T) => T]
  def countPredicates: List[T => Boolean]
  def forallPredicates: List[T => Boolean]
  def existsPredicates: List[T => Boolean]
  def findPredicates: List[T => Boolean]
  def mapFunctions: List[T => T]
  def partialMapFunctions: List[PartialFunction[T, T]]
  def flatMapFunctions: List[T => Iterable[T]]
  def filterPredicates: List[T => Boolean]
  def filterNotPredicates: List[T => Boolean]
  def partitionPredicates: List[T => Boolean]
  def takeWhilePredicates: List[T => Boolean]
  def dropWhilePredicates: List[T => Boolean]
  def spanPredicates: List[T => Boolean]
  def foldArguments: List[(T, (T, T) => T)]
  def addAllIterables: List[Iterable[T]]
  def newArray(sz: Int): Array[T]
  def groupByFunctions: List[T => T]
}

trait SeqOperators[T] extends Operators[T] {
  def segmentLengthPredicates: List[T => Boolean]
  def indexWherePredicates: List[T => Boolean]
  def lastIndexWherePredicates: List[T => Boolean]
  def reverseMapFunctions: List[T => T]
  def sameElementsSeqs: List[Seq[T]]
  def startEndSeqs: List[Seq[T]]
}
