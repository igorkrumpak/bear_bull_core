import NumberFormat from 'react-number-format';
import { useNavigate } from 'react-router-dom';
import Table from 'antd/es/table/Table';
import { Image, Card } from 'antd';
import TooltipText from '../custom/TooltipText';
import { IDashboard } from '../../rest/Interfaces';
import { useDashboardData } from './useDashboardData';
import { navigateToCoinChart } from '../../utils/NavigationUtils';
import CoinTags from '../custom/CoinTags';
import TooltipDashboardChart from '../custom/TooltipDashboardChart';




function Dashboard() {
    const navigate = useNavigate();
    const coins = useDashboardData();
    const columns = [
        {
            title: '#',
            render: (text: string, record: IDashboard, index: number) => record.position,
            width: '25px',
        },
        {
            title: 'Coin',
            render: (text: string, record: IDashboard, index: number) =>
                <div className='pointer' onClick={() => navigateToCoinChart(navigate, record.coinId)} style={{
                    display: 'flex'
                }}>
                    <Image width='19px' height='19px' src={record.coinThumbImage} preview={false} />
                    <span style={{ fontWeight: 'bold', paddingLeft: '3px' }}>{record.coinName}</span>
                    {"(" + record.coinSymbol.toUpperCase() + ")"}
                </div>,
            width: '250px'
        },
        {
            title: 'Tags',
            render: (text: string, record: IDashboard, index: number) =>
                <CoinTags tags={record.tags} />
            ,
            width: 'auto'
        },

        {
            title: 'Price',
            render: (text: string, record: IDashboard, index: number) =>
                <NumberFormat displayType={'text'} value={record.price} prefix={'$'} thousandSeparator={true} />,
            width: '150px',
        },
        {
            title: '% Last Period',
            render: (text: string, record: IDashboard, index: number) =>
                <NumberFormat displayType={'text'} value={record.percentFromLastPeriod} suffix={'%'} thousandSeparator={false} />,
            width: '60px',
        },
        {
            title: '% ATH',
            render: (text: string, record: IDashboard, index: number) =>
                <NumberFormat displayType={'text'} value={record.percentFromATH} suffix={'%'} thousandSeparator={false} />,
            width: '60px',
        },
        {
            title: 'Mkt. Cap',
            render: (text: string, record: IDashboard, index: number) =>
                <NumberFormat displayType={'text'} value={record.marketCap.toFixed()} prefix={'$'} thousandSeparator={true} />,
            width: '150px',
        },
        {
            title: '24h Volume',
            render: (text: string, record: IDashboard, index: number) =>
                <NumberFormat displayType={'text'} value={record.totalVolume.toFixed()} prefix={'$'} thousandSeparator={true} />,
            width: '150px',
        },
        {
            title: (
                <>
                    Oscillators{' '}
                    <TooltipText notation='Oscillator_Chart' />
                </>
            ),
            render: (text: string, record: IDashboard, index: number) =>
                <TooltipDashboardChart chart={record.oscillatorsChart} values={[
                    {color: record.rsiChartColor, label : record.rsiLabel, value : record.rsi}, 
                    {color: record.mfiChartColor, label : record.mfiLabel, value : record.mfi}, 
                    {color: record.stochasticOscillatorChartColor, label : record.stochasticOscillatorLabel, value : record.stochasticOscillator}]} />,
            align: 'center' as 'center',
            width: '140px',
        },
        {
            title: (
                <>
                    Bollinger Bands{' '}
                    <TooltipText notation='Bollinger_Bands_Chart' />
                </>
            ),
            render: (text: string, record: IDashboard, index: number) =>
                <TooltipDashboardChart chart={record.bollingerBandsChart} values={[
                    {color: record.lowerBandChartColor, label : record.lowerBandLabel, value : record.lowerBand},
                    {color: record.upperBandChartColor, label : record.upperBandLabel, value : record.upperBand}, 
                    {color: record.avgPrice20PeriodsChartColor, label : record.avgPrice20PeriodsLabel, value : record.avgPrice20Periods}, 
                    {color: record.priceChartColor, label : record.priceLabel, value : record.price}]} />,
            align: 'center' as 'center',
            width: '140px',
        },
        {
            title: (
                <>
                    Mvg. Avarages{' '}
                    <TooltipText notation='Moving_Avarages_Chart' />
                </>
            ),
            render: (text: string, record: IDashboard, index: number) =>
                <TooltipDashboardChart chart={record.movingAvaragesChart} values={[
                    {color: record.avgPrice50PeriodsChartColor, label : record.avgPrice50PeriodsLabel, value : record.avgPrice50Periods}, 
                    {color: record.avgPrice200PeriodsChartColor, label : record.avgPrice200PeriodsLabel, value : record.avgPrice200Periods}, 
                    {color: record.priceChartColor, label : record.priceLabel, value : record.price}]} />,
            align: 'center' as 'center',
            width: '140px',
        },
    ];

    return (
        <div>
            <Card>
                <Table
                    dataSource={coins}
                    columns={columns}
                    size="small"
                    bordered={true}
                />
            </Card>
        </div>)
}

export default Dashboard;
