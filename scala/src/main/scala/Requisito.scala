trait Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean
}

object RequisitoPeso extends Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.peso < dragon.cuantoPuedeCargar
}

case class RequisitoBarbarosidad (minimaBarbarosidad: Int) extends Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.barbarosidad >= minimaBarbarosidad
}

case class RequisitoItemParticular (item: Item) extends Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.item.equals(item)
}
//TODO ver que hacer con los requisitos de dragon y los de posta
object RequisitoDanio extends Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = dragon.danio > vikingo.danioQueProduce
}

case class RequisitoPesoDeterminado (pesoDeterminado: Int) extends Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.peso <= pesoDeterminado
}

trait RequisitoPosta{
  def cumpleRequisito(competidor: Competidor): Boolean
}

case class RequisitoPesoMinimo (pesoMinimo: Int) extends RequisitoPosta {
  def cumpleRequisito(competidor: Competidor): Boolean = competidor.cuantoPuedeCargar > pesoMinimo
}

case class RequisitoBarbarosidadPosta (minimaBarbarosidad: Int) extends RequisitoPosta {
  def cumpleRequisito(competidor: Competidor): Boolean = competidor.barbarosidad >= minimaBarbarosidad
}

case object RequisitoArma extends RequisitoPosta {
  def cumpleRequisito(competidor: Competidor): Boolean = competidor.tieneArma
}

case object RequisitoMontura extends RequisitoPosta {
  def cumpleRequisito(competidor: Competidor): Boolean = competidor.isInstanceOf [Jinete]
}