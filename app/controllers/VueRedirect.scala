package controllers

import com.google.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class VueRedirect @Inject()(assets: Assets,
                              cc: ControllerComponents)
                             (implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def matchAll(resource: String) = assets.at("index.html")
}