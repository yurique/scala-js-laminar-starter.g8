package scalajsjest

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.|

@JSGlobal("window")
@js.native
object JestGlobal extends js.Object {

  def test(str: String, function: js.Function0[Any]): Unit = js.native

  def describe(str: String, function: js.Function0[Any]): js.Any = js.native

  def afterEach(function: js.Function0[Any]): Unit = js.native

  def beforeEach(function: js.Function0[Any]): Unit = js.native

  def beforeAll(function: js.Function0[Any]): Unit = js.native

  def afterAll(function: js.Function0[Any]): Unit = js.native

  val test: JestTestObject = js.native

  val describe: JestTestObject = js.native

  def expect[T](in: T): Matcher[T] = js.native

  @JSName("expect")
  def expectAsync[T](in: js.Promise[T]): MatcherAsync[T] = js.native

  val expect: ExpectObject = js.native

}

@js.native
trait ExpectObject extends js.Object {

  def extend(matchers: js.Object): js.Any = js.native

  def assertions(in: Int): js.Object                       = js.native
  def hasAssertions(): js.Object                           = js.native
  def objectContaining(obj: js.Object): js.Object          = js.native
  def stringContaining(str: String): js.Object             = js.native
  def stringMatching(regexp: js.RegExp): js.Object         = js.native
  def addSnapshotSerializer(serializer: js.Any): js.Object = js.native

}

@js.native
trait JestTestObject extends js.Object {

  def only(str: String, function: js.Function0[_]): Unit = js.native

  def skip(str: String, function: js.Function0[_]): Unit = js.native
}

@js.native
trait Matcher[T] extends js.Object {

  def toBe(in: T): js.Object                                 = js.native
  def toBeCloseTo(in: T): js.Object                          = js.native
  def toBeDefined(): js.Object                               = js.native
  def toBeFalsy(): js.Object                                 = js.native
  def toBeGreaterThan(numb: Double): js.Object               = js.native
  def toBeGreaterThanOrEqual(numb: Double): js.Object        = js.native
  def toBeLessThan(numb: Double): js.Object                  = js.native
  def toBeLessThanOrEqual(numb: Double): js.Object           = js.native
  def toBeInstanceOf(in: AnyRef): js.Object                  = js.native
  def toBeNull(): js.Object                                  = js.native
  def toBeTruthy(): js.Object                                = js.native
  def toBeUndefined(): js.Object                             = js.native
  def toContain(item: js.Any): js.Object                     = js.native
  def toContainEqual(item: js.Any): js.Object                = js.native
  def toEqual(value: Any): js.Object                         = js.native
  def toHaveLength(number: Int): js.Object                   = js.native
  def toMatch(regexpOrString: String | js.RegExp): js.Object = js.native
  def toMatchObject(in: Any): js.Object                      = js.native

  def toHaveProperty(keyPath: String, value: js.Any = ???): js.Object =
    js.native
  def toMatchSnapshot(optStr: String = ???): js.Object = js.native
  def toThrow(error: Any = ???): js.Object             = js.native
  def toThrowError(error: Any = ???): js.Object        = js.native
  def toThrowErrorMatchingSnapshot(): js.Object        = js.native
  val not: Matcher[T]                                  = js.native

}

@js.native
trait MatcherAsync[T] extends Matcher[T] {
  val resolves: Matcher[T] = js.native
  val rejects: Matcher[T]  = js.native
}

@js.native
trait JestObject extends js.Object {
  def clearAllTimers(): Unit                                                                       = js.native
  def clearAllMocks(): JestObject                                                                  = js.native
  def resetAllMocks(): JestObject                                                                  = js.native
  def useFakeTimers(): JestObject                                                                  = js.native
  def useRealTimers(): JestObject                                                                  = js.native
  def runAllTicks(): JestObject                                                                    = js.native
  def runTimersToTime(msToRun: Int): JestObject                                                    = js.native
  def setTimeout(timeout: Int): JestObject                                                         = js.native
  def runAllImmediates(): JestObject                                                               = js.native
  def runAllTimers(): JestObject                                                                   = js.native
  def runOnlyPendingTimers(): JestObject                                                           = js.native
  def resetModules(): JestObject                                                                   = js.native
  def disableAutomock(): JestObject                                                                = js.native
  def enableAutomock(): JestObject                                                                 = js.native
  def fn(implementation: js.Function): js.Function                                                 = js.native
  def isMockFunction(fn: js.Function): Boolean                                                     = js.native
  def genMockFromModule(moduleName: String): js.Function                                           = js.native
  def mock(moduleName: String, factory: js.Function = ???, options: js.Object = ???): JestObject   = js.native
  def doMock(moduleName: String, factory: js.Function = ???, options: js.Object = ???): JestObject = js.native
  def unmock(moduleName: String): JestObject                                                       = js.native
  def dontMock(moduleName: String): JestObject                                                     = js.native

  def setMock(moduleName: String, moduleExports: js.Any): JestObject =
    js.native
  def spyOn(obj: js.Object, methodName: String): JestObject = js.native
}

@JSGlobal("jest")
@js.native
object Jest extends JestObject
