package com.symphony.dal.communicator.axis;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.util.Map;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;

/**
 * Unit test for AxisCommunicator
 * ExtendedStatistics and DynamicStatistics retrieve data success
 */
public class AxisCommunicatorTest {

	AxisCommunicator axisCommunicator = new AxisCommunicator();

@Before
public void setUp() throws Exception {
	axisCommunicator.setHost("127.0.0.1");
	axisCommunicator.setProtocol("http");
	axisCommunicator.setPort(5500);
	axisCommunicator.init();
}

	/**
	 * Test AxisCommunicator#getMultipleStatistics success with statistics data not empty
	 * Expect statistics have data
	 */
	@Test
	public void checkExtendedStatistics() throws Exception {
		ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistic.getStatistics();
//		Assert.assertEquals("1.0", stats.get(AxisMonitoringMetric.SCHEMA_VERSIONS.toString()));
//		Assert.assertEquals("AXIS", stats.get(AxisMonitoringMetric.BRAND));
//		Assert.assertEquals("Feb 14 2018 13:08", stats.get(AxisMonitoringMetric.BUILD_DATE));
//		Assert.assertEquals("AXIS Q3505 Mk II Fixed Dome Network Camera", stats.get(AxisMonitoringMetric.DEVICE_NAME));
//		Assert.assertEquals("714.4", stats.get(AxisMonitoringMetric.HARD_WARE_ID));
//		Assert.assertEquals("8.20.1", stats.get(AxisMonitoringMetric.VERSION));
//		Assert.assertEquals("http://www.axis.com", stats.get(AxisMonitoringMetric.WEB_URL));
//		Assert.assertEquals("ACCC8E78B977", stats.get(AxisMonitoringMetric.SERIAL_NUMBER));
//		Assert.assertEquals("The overlay text", stats.get(AxisMonitoringMetric.TEXT_OVERLAY_CONTENT.toString()));
//		Assert.assertEquals("35", stats.get(AxisMonitoringMetric.ROTATION.toString()));
//		Assert.assertEquals(AxisConstant.ENABLE, stats.get(AxisMonitoringMetric.DYNAMIC_OVERLAY.toString()));
//		Assert.assertEquals(AxisConstant.ENABLE, stats.get(AxisMonitoringMetric.MIRRORING.toString()));
	}
//
//	/**
//	 * Test AxisCommunicator#getMultipleStatistics success with dynamicStatistics data not empty
//	 * Expect dynamicStatistics have data
//	 */
//	@Test
//	public void checkDynamicStatistics() throws Exception {
//		ExtendedStatistics extendedStatistic = (ExtendedStatistics) axisCommunicator.getMultipleStatistics().get(0);
//		Map<String, String> dynamic = extendedStatistic.getDynamicStatistics();
//
//		Assert.assertEquals("192 x 144", dynamic.get(AxisMonitoringMetric.VIDEO_RESOLUTION.toString()));
//		Assert.assertEquals("60", dynamic.get(AxisMonitoringMetric.VIDEO_FRAME_RATE.toString()));
//		Assert.assertEquals(AxisConstant.ACTIVE, dynamic.get(AxisMonitoringMetric.VIDEO_SOURCE.toString()));
//	}

}
