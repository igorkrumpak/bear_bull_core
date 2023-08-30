import NumberFormat from 'react-number-format';
import { useNavigate } from 'react-router-dom';
import Table from 'antd/es/table/Table';
import { Tag, Image, Card } from 'antd';
import TooltipText from '../custom/TooltipText';
import { IDashboard } from '../../rest/Interfaces';
import { useDashboardData } from './useDashboardData';
import { navigateToCoinChart } from '../../utils/NavigationUtils';
import CoinTags from '../custom/CoinTags';



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
            title: '% 24h',
            render: (text: string, record: IDashboard, index: number) =>
                <NumberFormat displayType={'text'} value={record.percentFromYesterday} suffix={'%'} thousandSeparator={false} />,
            width: '60px',
        },
        {
            title: '% 7d',
            render: (text: string, record: IDashboard, index: number) =>
                <NumberFormat displayType={'text'} value={record.percentFromLastWeek} suffix={'%'} thousandSeparator={false} />,
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
                <Image width='140px' height='60px' src={"/api/dashboard/picture/" + record.oscillatorsChart} preview={false} />,
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
                <Image width='140px' height='60px' src={"/api/dashboard/picture/" + record.bollingerBandsChart} preview={false} />,
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
                <Image width='140px' height='60px' src={"/api/dashboard/picture/" + record.movingAvaragesChart} preview={false} />,
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