import { useRef, useState } from "react";
import {
  Button,
  Card,
  Col,
  Radio,
  Row,
  Typography,
  Cascader,
  Space,
} from "antd";
import { RedoOutlined } from "@ant-design/icons";
import { CascadeOption } from "../../rest/Interfaces";
import { useLocation } from "react-router-dom";
import { Chart } from "./Chart";
import { generateDefaultCharts } from "./chartUtils";
import { useCoinChartData } from "./useCoinChartData";
import { Chart as ChartJS } from 'chart.js';
import CoinTags from "../custom/CoinTags";

function Coin() {
  const { search } = useLocation();
  const coin = new URLSearchParams(search).get("coinId");

  const [selectedCharts, setSelectedCharts] = useState<string[][]>();

  const chartReference = useRef<ChartJS<"line", number[], string>>(null);

  const { chart, getDetails } = useCoinChartData(coin);


  const onPeriodChange = (e: any) => {
    getDetails(parseInt(e.target.value));
  };

  const onCascaderChange = (value: any) => {
    setSelectedCharts(value);
  };

  const resetZoom = () => {
    chartReference.current?.resetZoom();
  }

  return (
    <div className="card-container" style={{ maxWidth: "1200px", width: "100%", margin: "0 auto" }}>
      {chart && (
        <>
          <Space>
            <Typography.Title>{chart.title}</Typography.Title>
            <CoinTags tags={chart.tags} />
          </Space>
          <Card>
            <Row gutter={[16, 16]}>
              <Col span={12}>
                <Cascader
                  defaultValue={generateDefaultCharts(chart.xValues)}
                  style={{ width: "100%" }}
                  options={chart.xValues.map<CascadeOption>(each => ({ label: each.label, value: each.label }))}
                  multiple
                  maxTagCount="responsive"
                  onChange={onCascaderChange}
                />
              </Col>
              <Col span={12}>
                <Radio.Group defaultValue="30" buttonStyle="solid" onChange={onPeriodChange}>
                  <Radio.Button value="7">7 Days</Radio.Button>
                  <Radio.Button value="14">14 Days</Radio.Button>
                  <Radio.Button value="30">30 Days</Radio.Button>
                  <Radio.Button value="180">180 Days</Radio.Button>
                  <Radio.Button value="365">1 Year</Radio.Button>
                  <Radio.Button value="99999">Max</Radio.Button>
                </Radio.Group>
                <Button shape="circle" icon={<RedoOutlined />} style={{ marginLeft: 8 }} onClick={resetZoom} />
              </Col>
            </Row>
            <Row style={{ marginTop: 16 }}>
              <Col span={24}>
                <div style={{ width: "100%", height: "500px" }}>
                  <Chart chart={chart} selectedCharts={selectedCharts} chartReference={chartReference} />
                </div>
              </Col>
            </Row>
          </Card>
        </>
      )}
    </div>
  );
}

export default Coin;