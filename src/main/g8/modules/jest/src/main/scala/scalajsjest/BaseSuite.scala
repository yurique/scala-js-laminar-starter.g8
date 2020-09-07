package scalajsjest

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.JSConverters._

private[scalajsjest] trait BaseSuite {

  def test(name: String)(block: => Unit): Unit =
    JestGlobal.test(name, () => block)

  def testAsync(name: String)(block: => Any): Unit =
    JestGlobal.test(name, () => block)

  def testOnly(name: String)(block: => Unit): Unit =
    JestGlobal.test.only(name, () => block)

  def testSkip(name: String)(block: => Unit): Unit =
    JestGlobal.test.skip(name, () => block)

  def testOnlyAsync(name: String)(block: => Any): Unit =
    JestGlobal.test.only(name, () => block)

  def expect[T](value: T) = JestGlobal.expect(value)

  def expectAsync[T](value: Future[T]) =
    JestGlobal.expectAsync(value.toJSPromise)

  def assertions(num: Int) = JestGlobal.expect.assertions(num)

  def beforeEach(block: => Any) = JestGlobal.beforeEach(() => block)

  def afterEach(block: => Any) = JestGlobal.afterEach(() => block)

  def beforeAll(block: => Any) = JestGlobal.beforeAll(() => block)

  def afterAll(block: => Any) = JestGlobal.afterAll(() => block)
}
