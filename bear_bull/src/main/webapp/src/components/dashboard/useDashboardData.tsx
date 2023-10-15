import { useState, useEffect } from 'react';
import { IDashboard } from '../../rest/Interfaces';
import { fetchDashboardData } from '../../rest/apiUtils';
import { useGlobalState } from '../../state/GlobalStateProvider';

export const useDashboardData = () => {
  const [coins, setCoins] = useState<IDashboard[]>();
  const { state } = useGlobalState();

  useEffect(() => {
    const getCoins = async () => {
      const data = await fetchDashboardData(state.timeframe);
      setCoins(data);
    };

    getCoins();
  }, [state]);

  return coins;
};