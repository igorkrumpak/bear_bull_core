return MathUtils.round2DecimalPlaces(((o.getDouble('Price') / o.getPreviousCoinDataObject(1).getDouble('Price') * 100)) - 100);