sealed trait Regla

case object Estandar extends Regla
case class Eliminacion(cantidadAEliminar: Int) extends Regla
case object TorneoInverso extends Regla
case class BanDeDragones(condicion: Dragon => Boolean) extends Regla
case object Handicap extends Regla
case object PorEquipos extends Regla
