package frc.paths;

import com.team319.trajectory.Path;

public class Test extends Path {
   // dt,x,y,left.pos,left.vel,left.acc,left.jerk,center.pos,center.vel,center.acc,center.jerk,right.pos,right.vel,right.acc,right.jerk,heading
	private static final double[][] points = {
				{0.0200,5.9040,-3.7400,0.0040,0.2000,10.0000,0.0000,0.0040,0.2000,10.0000,0.0000,0.0040,0.2000,10.0000,0.0000,0.0000},
				{0.0200,5.9120,-3.7400,0.0120,0.3995,9.9770,-1.1517,0.0120,0.4000,10.0000,0.0000,0.0120,0.4005,10.0230,1.1517,0.0000},
				{0.0200,5.9240,-3.7400,0.0240,0.5984,9.9455,-1.5736,0.0240,0.6000,10.0000,0.0000,0.0240,0.6016,10.0545,1.5736,0.0000},
				{0.0200,5.9400,-3.7400,0.0399,0.7963,9.8944,-2.5547,0.0400,0.8000,10.0000,0.0000,0.0401,0.8037,10.1056,2.5547,0.0001},
				{0.0200,5.9600,-3.7400,0.0597,0.9929,9.8271,-3.3662,0.0600,1.0000,10.0000,0.0000,0.0603,1.0071,10.1729,3.3662,0.0003},
				{0.0200,5.9840,-3.7400,0.0835,1.1878,9.7442,-4.1450,0.0840,1.2000,10.0000,0.0000,0.0845,1.2122,10.2558,4.1450,0.0005},
				{0.0200,6.0120,-3.7400,0.1111,1.3807,9.6465,-4.8849,0.1120,1.4000,10.0000,0.0000,0.1129,1.4193,10.3535,4.8849,0.0009},
				{0.0200,6.0440,-3.7399,0.1425,1.5714,9.5349,-5.5808,0.1440,1.6000,10.0000,0.0000,0.1455,1.6286,10.4651,5.5808,0.0015},
				{0.0200,6.0800,-3.7399,0.1777,1.7596,9.4103,-6.2289,0.1800,1.8000,10.0000,0.0000,0.1823,1.8404,10.5897,6.2288,0.0023},
				{0.0200,6.1200,-3.7398,0.2166,1.9451,9.2738,-6.8264,0.2200,2.0000,10.0000,0.0000,0.2234,2.0549,10.7262,6.8262,0.0034},
				{0.0200,6.1640,-3.7396,0.2592,2.1276,9.1263,-7.3716,0.2640,2.2000,10.0000,0.0000,0.2688,2.2724,10.8737,7.3714,0.0048},
				{0.0200,6.2120,-3.7393,0.3053,2.3070,8.9690,-7.8638,0.3120,2.4000,10.0000,0.0000,0.3187,2.4930,11.0309,7.8635,0.0067},
				{0.0200,6.2640,-3.7389,0.3550,2.4830,8.8030,-8.3027,0.3640,2.6000,10.0000,0.0000,0.3730,2.7170,11.1970,8.3023,0.0090},
				{0.0200,6.3200,-3.7383,0.4081,2.6556,8.6292,-8.6880,0.4200,2.8000,10.0000,0.0000,0.4319,2.9444,11.3707,8.6874,0.0119},
				{0.0200,6.3800,-3.7375,0.4646,2.8246,8.4488,-9.0192,0.4800,3.0000,10.0000,0.0000,0.4954,3.1754,11.5511,9.0183,0.0154},
				{0.0200,6.4440,-3.7364,0.5244,2.9899,8.2630,-9.2945,0.5440,3.2000,10.0000,0.0000,0.5636,3.4101,11.7370,9.2932,0.0196},
				{0.0200,6.5120,-3.7349,0.5874,3.1513,8.0727,-9.5105,0.6120,3.4000,10.0000,0.0000,0.6366,3.6487,11.9271,9.5089,0.0246},
				{0.0200,6.5839,-3.7329,0.6536,3.3089,7.8795,-9.6613,0.6840,3.6000,10.0000,0.0000,0.7144,3.8911,12.1203,9.6591,0.0304},
				{0.0200,6.6599,-3.7303,0.7228,3.4626,7.6848,-9.7373,0.7600,3.8000,10.0000,0.0000,0.7972,4.1374,12.3150,9.7346,0.0372},
				{0.0200,6.7398,-3.7271,0.7951,3.6124,7.4903,-9.7248,0.8400,4.0000,10.0000,0.0000,0.8849,4.3876,12.5094,9.7214,0.0449},
				{0.0200,6.8237,-3.7229,0.8703,3.7584,7.2982,-9.6046,0.9240,4.2000,10.0000,0.0000,0.9777,4.6416,12.7014,9.6005,0.0537},
				{0.0200,6.9116,-3.7178,0.9483,3.9006,7.1112,-9.3516,1.0120,4.4000,10.0000,0.0000,1.0757,4.8994,12.8884,9.3466,0.0637},
				{0.0200,7.0033,-3.7114,1.0291,4.0392,6.9325,-8.9338,1.1040,4.6000,10.0000,0.0000,1.1789,5.1607,13.0669,8.9279,0.0749},
				{0.0200,7.0990,-3.7036,1.1125,4.1746,6.7662,-8.3122,1.2000,4.8000,10.0000,0.0000,1.2874,5.4254,13.2330,8.3054,0.0875},
				{0.0200,7.1986,-3.6942,1.1987,4.3069,6.6174,-7.4413,1.3000,5.0000,10.0000,0.0000,1.4013,5.6930,13.3817,7.4335,0.1013},
				{0.0200,7.3020,-3.6829,1.2874,4.4368,6.4920,-6.2701,1.4040,5.2000,10.0000,0.0000,1.5206,5.9631,13.5069,6.2614,0.1166},
				{0.0200,7.4091,-3.6694,1.3787,4.5647,6.3971,-4.7449,1.5120,5.4000,10.0000,0.0000,1.6453,6.2352,13.6017,4.7356,0.1333},
				{0.0200,7.5200,-3.6536,1.4725,4.6915,6.3408,-2.8136,1.6240,5.6000,10.0000,0.0000,1.7754,6.5083,13.6577,2.8039,0.1514},
				{0.0200,7.6345,-3.6349,1.5689,4.8182,6.3322,-0.4316,1.7400,5.8000,10.0000,0.0000,1.9111,6.7817,13.6662,0.4220,0.1711},
				{0.0200,7.7525,-3.6133,1.6678,4.9458,6.3808,2.4295,1.8600,6.0000,10.0000,0.0000,2.0522,7.0540,13.6174,-2.4385,0.1922},
				{0.0200,7.8740,-3.5882,1.7693,5.0757,6.4962,5.7724,1.9840,6.2000,10.0000,0.0000,2.1986,7.3240,13.5018,-5.7801,0.2147},
				{0.0200,7.9987,-3.5595,1.8735,5.2094,6.6875,9.5635,2.1120,6.4000,10.0000,0.0000,2.3504,7.5902,13.3104,-9.5690,0.2385},
				{0.0200,8.1265,-3.5267,1.9805,5.3487,6.9620,13.7231,2.2440,6.6000,10.0000,0.0000,2.5075,7.8510,13.0359,-13.7254,0.2635},
				{0.0200,8.2574,-3.4896,2.0904,5.4952,7.3244,18.1196,2.3800,6.8000,10.0000,0.0000,2.6695,8.1044,12.6736,-18.1180,0.2896},
				{0.0200,8.3910,-3.4478,2.2034,5.6507,7.7758,22.5701,2.5200,7.0000,10.0000,0.0000,2.8365,8.3489,12.2223,-22.5637,0.3166},
				{0.0200,8.5272,-3.4011,2.3198,5.8169,8.3128,26.8493,2.6640,7.2000,10.0000,0.0000,3.0082,8.5826,11.6855,-26.8378,0.3442},
				{0.0200,8.6658,-3.3492,2.4397,5.9955,8.9269,30.7092,2.8120,7.4000,10.0000,0.0000,3.1843,8.8040,11.0717,-30.6923,0.3723},
				{0.0200,8.8066,-3.2919,2.5634,6.1876,9.6051,33.9067,2.9640,7.6000,10.0000,0.0000,3.3645,9.0119,10.3940,-33.8849,0.4005},
				{0.0200,8.9494,-3.2290,2.6913,6.3942,10.3298,36.2371,3.1200,7.8000,10.0000,0.0000,3.5486,9.2053,9.6698,-36.2113,0.4287},
				{0.0200,9.0939,-3.1605,2.8236,6.6158,11.0811,37.5649,3.2800,8.0000,10.0000,0.0000,3.7363,9.3837,8.9190,-37.5364,0.4563},
				{0.0200,9.2401,-3.0863,2.9607,6.8526,11.8381,37.8472,3.4440,8.2000,10.0000,0.0000,3.9272,9.5469,8.1627,-37.8178,0.4833},
				{0.0200,9.3879,-3.0062,3.1028,7.1042,12.5809,37.1422,3.6120,8.4000,10.0000,0.0000,4.1211,9.6953,7.4204,-37.1137,0.5092},
				{0.0200,9.5370,-2.9205,3.2502,7.3700,13.2929,35.6019,3.7840,8.6000,10.0000,0.0000,4.3177,9.8295,6.7089,-35.5759,0.5338},
				{0.0200,9.6874,-2.8292,3.4031,7.6493,13.9619,33.4496,3.9600,8.8000,10.0000,0.0000,4.5167,9.9503,6.0403,-33.4278,0.5568},
				{0.0200,9.8392,-2.7325,3.5620,7.9409,14.5809,30.9498,4.1400,9.0000,10.0000,0.0000,4.7179,10.0588,5.4217,-30.9331,0.5780},
				{0.0200,9.9923,-2.6304,3.7268,8.2439,15.1484,28.3744,4.3240,9.2000,10.0000,0.0000,4.9210,10.1558,4.8544,-28.3638,0.5971},
				{0.0200,10.1469,-2.5234,3.8980,8.5572,15.6679,25.9745,4.5120,9.4000,10.0000,0.0000,5.1259,10.2425,4.3350,-25.9704,0.6140},
				{0.0200,10.3030,-2.4116,4.0756,8.8802,16.1471,23.9589,4.7040,9.6000,10.0000,0.0000,5.3323,10.3197,3.8557,-23.9616,0.6283},
				{0.0200,10.4608,-2.2954,4.2598,9.2121,16.5967,22.4825,4.9000,9.8000,10.0000,0.0000,5.5400,10.3878,3.4059,-22.4921,0.6401},
				{0.0200,10.6207,-2.1752,4.4509,9.5527,17.0296,21.6411,5.1000,10.0000,10.0000,0.0000,5.7490,10.4472,2.9728,-21.6578,0.6491},
				{0.0200,10.7796,-2.0538,4.6450,9.7062,7.6747,-467.7406,5.3000,10.0000,10.0000,0.0000,5.9548,10.2938,-7.6729,-532.2801,0.6549},
				{0.0200,10.9380,-1.9317,4.8422,9.8591,7.6464,-1.4179,5.5000,10.0000,10.0000,0.0000,6.1576,10.1409,-7.6453,1.3785,0.6577},
				{0.0200,11.0963,-1.8095,5.0424,10.0117,7.6306,-0.7897,5.7000,10.0000,10.0000,0.0000,6.3574,9.9883,-7.6303,0.7507,0.6575},
				{0.0200,11.2548,-1.6874,5.2457,10.1644,7.6315,0.0472,5.9000,10.0000,10.0000,0.0000,6.5541,9.8356,-7.6320,-0.0860,0.6542},
				{0.0200,11.4138,-1.5662,5.4521,10.3173,7.6489,0.8666,6.1000,10.0000,10.0000,0.0000,6.7478,9.6826,-7.6501,-0.9056,0.6479},
				{0.0200,11.5738,-1.4462,5.6615,10.4709,7.6777,1.4418,6.3000,10.0000,10.0000,0.0000,6.9384,9.5290,-7.6797,-1.4812,0.6385},
				{0.0200,11.7319,-1.3303,5.8697,10.4110,-2.9922,-533.4947,6.4960,9.8000,-10.0000,0.0000,7.1221,9.1888,-17.0102,-466.5246,0.6262},
				{0.0200,11.8883,-1.2189,6.0765,10.3425,-3.4284,-21.8098,6.6880,9.6000,-10.0000,0.0000,7.2993,8.8573,-16.5743,21.7938,0.6114},
				{0.0200,12.0431,-1.1123,6.2818,10.2648,-3.8837,-22.7673,6.8760,9.4000,-10.0000,0.0000,7.4700,8.5350,-16.1192,22.7585,0.5941},
				{0.0200,12.1966,-1.0108,6.4854,10.1774,-4.3710,-24.3616,7.0600,9.2000,-10.0000,0.0000,7.6344,8.2223,-15.6320,24.3599,0.5745},
				{0.0200,12.3487,-0.9146,6.6870,10.0794,-4.9007,-26.4866,7.2400,9.0000,-10.0000,0.0000,7.7928,7.9203,-15.1021,26.4918,0.5529},
				{0.0200,12.4995,-0.8239,6.8864,9.9698,-5.4802,-28.9762,7.4160,8.8000,-10.0000,0.0000,7.9454,7.6298,-14.5224,28.9880,0.5295},
				{0.0200,12.6490,-0.7388,7.0833,9.8475,-6.1124,-31.6100,7.5880,8.6000,-10.0000,0.0000,8.0925,7.3520,-13.8898,31.6278,0.5046},
				{0.0200,12.7971,-0.6595,7.2776,9.7116,-6.7950,-34.1268,7.7560,8.4000,-10.0000,0.0000,8.2342,7.0879,-13.2068,34.1498,0.4783},
				{0.0200,12.9437,-0.5860,7.4688,9.5612,-7.5199,-36.2475,7.9200,8.2000,-10.0000,0.0000,8.3710,6.8383,-12.4813,36.2745,0.4511},
				{0.0200,13.0887,-0.5182,7.6567,9.3957,-8.2740,-37.7059,8.0800,8.0000,-10.0000,0.0000,8.5031,6.6037,-11.7266,37.7352,0.4232},
				{0.0200,13.2318,-0.4562,7.8410,9.2150,-9.0397,-38.2826,8.2360,7.8000,-10.0000,0.0000,8.6308,6.3845,-10.9604,38.3125,0.3949},
				{0.0200,13.3729,-0.3997,8.0214,9.0190,-9.7964,-37.8361,8.3880,7.6000,-10.0000,0.0000,8.7544,6.1805,-10.2031,37.8648,0.3665},
				{0.0200,13.5118,-0.3486,8.1975,8.8086,-10.5229,-36.3234,8.5360,7.4000,-10.0000,0.0000,8.8742,5.9909,-9.4761,36.3492,0.3383},
				{0.0200,13.6483,-0.3027,8.3692,8.5846,-11.1990,-33.8049,8.6800,7.2000,-10.0000,0.0000,8.9905,5.8150,-8.7996,33.8264,0.3106},
				{0.0200,13.7821,-0.2617,8.5362,8.3484,-11.8076,-30.4329,8.8200,7.0000,-10.0000,0.0000,9.1035,5.6511,-8.1906,30.4493,0.2837},
				{0.0200,13.9132,-0.2254,8.6982,8.1017,-12.3361,-26.4262,8.9560,6.8000,-10.0000,0.0000,9.2135,5.4979,-7.6618,26.4371,0.2576},
				{0.0200,14.0412,-0.1933,8.8552,7.8462,-12.7769,-22.0367,9.0880,6.6000,-10.0000,0.0000,9.3205,5.3535,-7.2210,22.0423,0.2327},
				{0.0200,14.1661,-0.1653,9.0068,7.5836,-13.1272,-17.5159,9.2160,6.4000,-10.0000,0.0000,9.4249,5.2161,-6.8707,17.5168,0.2090},
				{0.0200,14.2877,-0.1409,9.1532,7.3159,-13.3889,-13.0877,9.3400,6.2000,-10.0000,0.0000,9.5265,5.0839,-6.6090,13.0846,0.1867},
				{0.0200,14.4058,-0.1199,9.2940,7.0445,-13.5675,-8.9298,9.4600,6.0000,-10.0000,0.0000,9.6256,4.9553,-6.4305,8.9237,0.1658},
				{0.0200,14.5204,-0.1019,9.4295,6.7711,-13.6709,-5.1666,9.5760,5.8000,-10.0000,0.0000,9.7222,4.8287,-6.3273,5.1584,0.1464},
				{0.0200,14.6314,-0.0865,9.5594,6.4969,-13.7083,-1.8696,9.6880,5.6000,-10.0000,0.0000,9.8163,4.7029,-6.2901,1.8603,0.1284},
				{0.0200,14.7386,-0.0736,9.6839,6.2231,-13.6896,0.9348,9.7960,5.4000,-10.0000,0.0000,9.9078,4.5768,-6.3090,-0.9447,0.1120},
				{0.0200,14.8420,-0.0628,9.8029,5.9506,-13.6245,3.2557,9.9000,5.2000,-10.0000,0.0000,9.9968,4.4493,-6.3743,-3.2655,0.0970},
				{0.0200,14.9416,-0.0538,9.9165,5.6802,-13.5219,5.1266,10.0000,5.0000,-10.0000,0.0000,10.0832,4.3197,-6.4770,-5.1359,0.0834},
				{0.0200,15.0373,-0.0464,10.0247,5.4124,-13.3900,6.5953,10.0960,4.8000,-10.0000,0.0000,10.1669,4.1875,-6.6091,-6.6039,0.0711},
				{0.0200,15.1291,-0.0403,10.1277,5.1477,-13.2357,7.7161,10.1880,4.6000,-10.0000,0.0000,10.2480,4.0523,-6.7636,-7.7238,0.0602},
				{0.0200,15.2170,-0.0355,10.2254,4.8864,-13.0648,8.5438,10.2760,4.4000,-10.0000,0.0000,10.3263,3.9136,-6.9346,-8.5506,0.0504},
				{0.0200,15.3009,-0.0316,10.3180,4.6287,-12.8822,9.1297,10.3600,4.2000,-10.0000,0.0000,10.4017,3.7712,-7.1173,-9.1355,0.0419},
				{0.0200,15.3808,-0.0286,10.4055,4.3749,-12.6919,9.5189,10.4400,4.0000,-10.0000,0.0000,10.4742,3.6251,-7.3078,-9.5238,0.0344},
				{0.0200,15.4568,-0.0262,10.4880,4.1250,-12.4969,9.7499,10.5160,3.8000,-10.0000,0.0000,10.5437,3.4750,-7.5029,-9.7540,0.0279},
				{0.0200,15.5288,-0.0244,10.5656,3.8790,-12.2998,9.8538,10.5880,3.6000,-10.0000,0.0000,10.6101,3.3210,-7.7000,-9.8571,0.0223},
				{0.0200,15.5968,-0.0231,10.6383,3.6369,-12.1027,9.8548,10.6560,3.4000,-10.0000,0.0000,10.6734,3.1631,-7.8972,-9.8575,0.0175},
				{0.0200,15.6608,-0.0221,10.7063,3.3988,-11.9073,9.7711,10.7200,3.2000,-10.0000,0.0000,10.7334,3.0012,-8.0926,-9.7732,0.0136},
				{0.0200,15.7208,-0.0214,10.7696,3.1645,-11.7149,9.6156,10.7800,3.0000,-10.0000,0.0000,10.7901,2.8355,-8.2850,-9.6172,0.0103},
				{0.0200,15.7768,-0.0209,10.8282,2.9339,-11.5270,9.3969,10.8360,2.8000,-10.0000,0.0000,10.8434,2.6661,-8.4729,-9.3981,0.0076},
				{0.0200,15.8288,-0.0205,10.8824,2.7070,-11.3446,9.1202,10.8880,2.6000,-10.0000,0.0000,10.8933,2.4930,-8.6554,-9.1211,0.0054},
				{0.0200,15.8768,-0.0203,10.9321,2.4837,-11.1688,8.7883,10.9360,2.4000,-10.0000,0.0000,10.9396,2.3163,-8.8311,-8.7889,0.0038},
				{0.0200,15.9208,-0.0202,10.9773,2.2637,-11.0008,8.4021,10.9800,2.2000,-10.0000,0.0000,10.9823,2.1363,-8.9992,-8.4026,0.0025},
				{0.0200,15.9608,-0.0201,11.0183,2.0468,-10.8416,7.9619,11.0200,2.0000,-10.0000,0.0000,11.0214,1.9532,-9.1584,-7.9622,0.0016},
				{0.0200,15.9968,-0.0200,11.0549,1.8330,-10.6922,7.4675,11.0560,1.8000,-10.0000,0.0000,11.0567,1.7670,-9.3078,-7.4677,0.0009},
				{0.0200,16.0288,-0.0200,11.0874,1.6219,-10.5538,6.9188,11.0880,1.6000,-10.0000,0.0000,11.0883,1.5781,-9.4462,-6.9189,0.0005},
				{0.0200,16.0568,-0.0200,11.1156,1.4133,-10.4275,6.3164,11.1160,1.4000,-10.0000,0.0000,11.1160,1.3867,-9.5725,-6.3164,0.0002},
				{0.0200,16.0808,-0.0200,11.1398,1.2071,-10.3143,5.6619,11.1400,1.2000,-10.0000,0.0000,11.1399,1.1929,-9.6857,-5.6619,0.0001},
				{0.0200,16.1008,-0.0200,11.1598,1.0028,-10.2151,4.9580,11.1600,1.0000,-10.0000,0.0000,11.1598,0.9972,-9.7849,-4.9580,0.0000},
				{0.0200,16.1100,-0.0200,11.1691,0.4624,-27.0162,-840.0555,11.1692,0.8000,-10.0000,0.0000,11.1691,0.4618,-26.7708,-849.2972,0.0000},
				{0.0200,16.1100,-0.0200,11.1691,0.0000,-23.1219,194.7180,11.1692,0.6000,-10.0000,0.0000,11.1691,0.0000,-23.0911,183.9875,0.0000},
				{0.0200,16.1100,-0.0200,11.1691,0.0000,0.0000,1156.0927,11.1692,0.4000,-10.0000,0.0000,11.1691,0.0000,0.0000,1154.5545,0.0000},
				{0.0200,16.1100,-0.0200,11.1691,0.0000,0.0000,0.0000,11.1692,0.2000,-10.0000,0.0000,11.1691,0.0000,0.0000,0.0000,0.0000},
				{0.0200,16.1100,-0.0200,11.1691,0.0000,0.0000,0.0000,11.1692,0.0000,-10.0000,0.0000,11.1691,0.0000,0.0000,0.0000,0.0000},
				{0.0200,16.1100,-0.0200,11.1691,0.0000,0.0000,0.0000,11.1692,-0.2000,-10.0000,0.0000,11.1691,0.0000,0.0000,0.0000,0.0000},

	    };

	@Override
	public double[][] getPath() {
	    return points;
	}
}