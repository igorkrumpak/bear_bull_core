import { useState, useEffect } from 'react';
import { IDashboard } from '../../rest/Interfaces';
import { fetchDashboardData } from '../../rest/apiUtils';

export const useDashboardData = () => {
  const [coins, setCoins] = useState<IDashboard[]>();

  useEffect(() => {
    const getCoins = async () => {
      const data = await fetchDashboardData();
      setCoins(data);
    };

    getCoins();
  }, []);

  return coins;
};