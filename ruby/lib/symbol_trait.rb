module TraitSymbol
  refine Symbol do
    def >> un_simbolo
      {:nuevo_nombre => un_simbolo, :metodo_copiado => self}
    end
  end
end