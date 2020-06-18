class Item {
  def danioQueProduce: Double = 0
}

case class Arma (_danioQueProduce: Double) extends Item {
  override def danioQueProduce: Double = _danioQueProduce
}

case class Comestible (calorias: Double) extends Item {
  def hambreQueDisminuye: Double = calorias
}