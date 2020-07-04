abstract class Dragon (val peso: Int) {
  val danio: Int
  val requisitos: Set[RequisitoDragon] = requisitoBase ++ requisitosEspecificos

  def requisitoBase: Set[RequisitoDragon] = Set(RequisitoPeso)

  def requisitosEspecificos: Set[RequisitoDragon]

  def velocidadBase: Double = 60

  def velocidadDeVuelo: Double = velocidadBase - peso

  def cuantoPuedeCargar: Double = 0.2 * peso

  def puedeSerMontadoPor(vikingo: Vikingo): Boolean = requisitos.forall(_.apply(this, vikingo))
}

case class FuriaNocturna (_peso: Int, _danio: Int, item: Item) extends Dragon(_peso) {
  override def requisitosEspecificos: Set[RequisitoDragon] = Set(RequisitoItemParticular(item))
  override val danio: Int = _danio
  override def velocidadDeVuelo: Double = super.velocidadDeVuelo * 3
}

case class NadderMortifero (_peso: Int) extends Dragon(_peso) {
  override def requisitosEspecificos: Set[RequisitoDragon] = Set(RequisitoDanio)
  override val danio: Int = 150
}

case class Gronckle (_peso: Int, _pesoDeterminado: Int) extends Dragon(_peso) {
  override def requisitosEspecificos: Set[RequisitoDragon] = Set(RequisitoPesoDeterminadoDragon(_pesoDeterminado))
  override val danio: Int = peso * 5
  override def velocidadBase: Double = super.velocidadBase / 2
}


