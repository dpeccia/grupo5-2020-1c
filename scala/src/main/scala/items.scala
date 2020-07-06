trait Item {
  def danioQueProduce: Double = 0
  def esArma(): Boolean = false
}

case class Arma (_danioQueProduce: Double) extends Item {
  override def danioQueProduce: Double = _danioQueProduce

  override def esArma(): Boolean = true
}

case class Comestible (calorias: Double) extends Item {
  def hambreQueDisminuye: Double = calorias
}

case object SistemaDeVuelo extends Item