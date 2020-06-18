abstract class Dragon (_peso: Int) {
  var peso: Int = _peso
  var danio: Int
  var requisitos: Set[RequisitoDragon] = Set(RequisitoPeso)

  def velocidadBase: Double = 60

  def agregarRequisito(requisito: RequisitoDragon): Unit = requisitos += requisito

  def velocidadDeVuelo: Double = velocidadBase - peso

  def cuantoPuedeCargar: Double = 0.2 * peso

  def puedeSerMontadoPor(vikingo: Vikingo): Boolean = requisitos.forall(_.cumpleRequisito(this, vikingo))
}

case class FuriaNocturna (_peso: Int, _danio: Int, item: Option[Item]) extends Dragon(_peso) {
  if(item.isDefined)
    agregarRequisito(RequisitoItemParticular(item.get))
  override var danio: Int = _danio
  override def velocidadDeVuelo: Double = super.velocidadDeVuelo * 3 // TODO preguntar si es triple de la base o no
}

case class NadderMortifero (_peso: Int) extends Dragon(_peso) {
  agregarRequisito(RequisitoDanio)
  override var danio: Int = 150
}

case class Gronckle (_peso: Int, _pesoDeterminado: Int) extends Dragon(_peso) {
  agregarRequisito(RequisitoPesoDeterminado(_pesoDeterminado))
  override var danio: Int = peso * 5
  override def velocidadBase: Double = super.velocidadBase / 2
}


