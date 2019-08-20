package controllers.util

import com.google.inject.Inject
import controllers.Assets
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class VueRedirect @Inject()(assets: Assets,
                              cc: ControllerComponents)
                             (implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  /**
    * Method to catch all requests that do not match anything in the routes file.
    * Will return the index.html. this allows vue to handle all not found errors.
    *
    * @param resource   path of the resource being requested.
    */
  def matchAll(resource: String) = assets.at("index.html")
}