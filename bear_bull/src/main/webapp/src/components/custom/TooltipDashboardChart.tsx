import { Image, Tooltip } from 'antd';
import { TooltipDashboardChartValues } from '../../rest/Interfaces';

function TooltipDashboardChart({ chart, values }: { chart: number, values?: TooltipDashboardChartValues[]  }) {


    const title = 
    <table>
        {values?.map((item, index) => (
        <tr>
          <td>
            <div style={{ width: '16px', height: '16px', backgroundColor: item.color }}></div>
          </td>
          <td style={{  maxWidth: '160px', whiteSpace: 'nowrap', overflow:'hidden', textOverflow:'ellipsis' }}> {item.label}</td>
          <td>: {item.value}</td>
        </tr>
        ))}
    </table>


    return (
        <Tooltip  title= {title}>
            <Image width='140px' height='60px' src={"/api/dashboard/picture/" + chart} preview={false} />
        </Tooltip>
    )
}
export default TooltipDashboardChart;