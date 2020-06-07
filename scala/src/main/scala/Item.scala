class Item {
  def danioQueProduce: Double = 0
}

class Arma (_danioQueProduce: Double) extends Item {
  override def danioQueProduce: Double = _danioQueProduce
}

class Comestible (calorias: Double) extends Item {
  def hambreQueDisminuye: Double = calorias
}