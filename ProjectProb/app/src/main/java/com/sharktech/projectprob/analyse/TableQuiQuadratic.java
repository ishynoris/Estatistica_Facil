package com.sharktech.projectprob.analyse;

class TableQuiQuadratic {

    private static final float TABLE[][] = new float[][]{
            new float[]{0.0001f, 0.0002f, 0.0010f, 0.0039f, 0.0158f, 0.0642f, 0.1015f, 1.3233f, 1.6424f, 2.7055f, 3.8415f, 5.0239f, 6.6349f, 7.8794f},
            new float[]{0.0100f, 0.0201f, 0.0560f, 0.1026f, 0.2107f, 0.4463f, 0.5754f, 2.7762f, 3.2189f, 4.6052f, 5.9915f, 7.3778f, 9.2104f, 10.5965f},
            new float[]{0.0717f, 0.1148f, 0.2158f, 0.3518f, 0.5844f, 1.0052f, 1.2125f, 4.1083f, 4.6416f, 6.2514f, 7.8147f, 9.3484f, 11.3449f, 12.8381f},
            new float[]{0.2070f, 0.2971f, 0.4844f, 0.7107f, 1.0636f, 1.6488f, 1.9226f, 5.3853f, 5.9886f, 7.7794f, 9.4877f, 11.1433f, 13.2767f, 14.8602f},
            new float[]{0.4118f, 0.5543f, 0.8312f, 1.1455f, 1.6103f, 2.3425f, 2.6746f, 6.6257f, 7.2893f, 9.2363f, 11.0705f, 12.8325f, 15.0863f, 16.7496f},
            new float[]{0.6757f, 0.8721f, 1.2373f, 1.6354f, 2.2041f, 3.0701f, 3.4546f, 7.8408f, 8.5581f, 10.6446f, 12.5916f, 14.4494f, 16.8119f, 18.5474f},
            new float[]{0.9893f, 1.2390f, 1.6899f, 2.1673f, 2.8331f, 3.8223f, 4.2549f, 9.0371f, 9.8032f, 12.0170f, 14.0671f, 16.0128f, 18.4753f, 20.2777f},
            new float[]{1.3444f, 1.6465f, 2.1797f, 2.7326f, 3.4895f, 4.5936f, 5.0706f, 10.2189f, 11.0301f, 13.6316f, 15.5073f, 17.5345f, 20.0902f, 21.9549f},
            new float[]{1.7349f, 2.0879f, 2.7004f, 3.3251f, 4.1682f, 5.3801f, 5.8988f, 11.3887f, 12.2421f, 14.6837f, 16.9190f, 19.0228f, 21.6660f, 23.5893f},
            new float[]{2.1558f, 2.5582f, 3.2470f, 3.9403f, 4.4852f, 6.1791f, 6.7372f, 12.5489f, 13.4420f, 15.9872f, 18.3070f, 20.4832f, 23.2093f, 25.1881f},
            new float[]{2.6032f, 3.0535f, 3.8157f, 4.5748f, 5.5778f, 6.9887f, 7.5841f, 13.7007f, 14.6314f, 17.2750f, 19.6752f, 21.9200f, 24.7250f, 26.7569f},
            new float[]{3.0738f, 3.5706f, 4.4038f, 5.2260f, 6.3038f, 7.8073f, 8.4384f, 14.8454f, 15.8120f, 18.5493f, 21.0261f, 23.3367f, 26.2170f, 28.2997f},
            new float[]{3.5650f, 4.1069f, 5.0087f, 5.8919f, 7.0415f, 8.8339f, 9.2991f, 15.9839f, 16.9848f, 19.8119f, 22.3620f, 24.7356f, 27.6882f, 29.8193f},
            new float[]{4.0747f, 4.6604f, 5.6287f, 6.5706f, 7.7895f, 9.4673f, 10.1653f, 17.1169f, 18.1508f, 21.0641f, 23.6848f, 26.1189f, 29.1412f, 31.3194f},
            new float[]{4.6009f, 5.2294f, 6.2621f, 7.2609f, 8.5468f, 10.3070f, 11.0365f, 18.2451f, 19.3107f, 22.3071f, 24.9958f, 274884f, 30.5780f, 32.8015f},
            new float[]{5.1422f, 5.8122f, 6.9077f, 7.9616f, 9.3122f, 11.1521f, 11.9122f, 19.3689f, 20.4651f, 23.5418f, 26.2962f, 28.8453f, 31.9999f, 34.2671f},
            new float[]{5.6973f, 6.4077f, 7.5642f, 8.6718f, 10.0852f, 12.0023f, 12.7919f, 20.4887f, 21.6146f, 24.7690f, 27.5871f, 30.1910f, 33.4087f, 35.7184f},
            new float[]{6.2648f, 7.0149f, 8.2307f, 9.3904f, 10.8649f, 12.8570f, 13.6753f, 21.6049f, 22.7595f, 25.9894f, 28.8693f, 31.5264f, 34.8052f, 37.1564f},
            new float[]{6.8439f, 7.6327f, 8.9065f, 10.1170f, 11.6509f, 13.7158f, 14.5620f, 22.7178f, 23.9004f, 27.2036f, 30.1435f, 32.8523f, 36.1908f, 38.5821f},
            new float[]{7.4338f, 8.2604f, 9.5908f, 10.8508f, 12.4426f, 14.5784f, 15.4518f, 23.8277f, 25.0375f, 28.4120f, 31.1696f, 34.1696f, 37.5663f, 39.9969f},
            new float[]{8.0336f, 8.8972f, 10.2829f, 11.5913f, 13.2396f, 15.4446f, 16.3444f, 24.9348f, 26.1711f, 29.6151f, 32.6706f, 35.4789f, 38.9322f, 41.4009f},
            new float[]{8.6427f, 9.5425f, 10.9823f, 12.3380f, 14.0415f, 16.3140f, 17.2396f, 26.0393f, 27.3015f, 30.8133f, 33.9245f, 36.7807f, 40.2894f, 42.7957f},
            new float[]{9.2604f, 10.1957f, 11.6885f, 13.0905f, 14.8480f, 17.1865f, 18.1373f, 27.1413f, 28.4288f, 32.0069f, 35.1725f, 38.0756f, 41.6383f, 44.1814f},
            new float[]{9.8862f, 10.8563f, 12.4011f, 13.8484f, 15.6587f, 18.0618f, 19.0373f, 28.2412f, 29.5533f, 33.1962f, 36.4150f, 39.3641f, 42.9798f, 45.5584f},
            new float[]{10.5196f, 11.5240f, 13.1197f, 14.6144f, 16.4734f, 18.9397f, 19.9393f, 29.3388f, 30.6752f, 34.3816f, 37.6525f, 40.6465f, 44.3140f, 46.9280f},
            new float[]{11.1602f, 12.1982f, 13.8439f, 15.3792f, 17.2919f, 19.8202f, 20.8434f, 30.4346f, 31.7946f, 35.5632f, 38.8851f, 41.9231f, 45.6416f, 48.2898f},
            new float[]{11.8077f, 12.8785f, 14.5734f, 16.1514f, 18.1139f, 20.7030f, 21.7494f, 31.5284f, 32.9117f, 36.7412f, 40.1133f, 43.1945f, 46.9628f, 49.6450f},
            new float[]{12.4613f, 13.5647f, 15.3079f, 16.9279f, 18.9392f, 21.5880f, 22.6572f, 32.6205f, 34.0266f, 37.9159f, 41.3372f, 44.4608f, 48.2782f, 50.9936f},
            new float[]{13.1211f, 14.2564f, 16.0471f, 17.7084f, 19.7677f, 22.4751f, 23.5666f, 33.7109f, 35.1394f, 39.0875f, 42.5569f, 45.7223f, 49.5878f, 52.3355f},
            new float[]{13.7867f, 14.9535f, 16.7908f, 18.4927f, 20.5992f, 23.3641f, 24.4776f, 34.7997f, 36.2502f, 40.2560f, 43.7730f, 46.9792f, 50.8922f, 53.6719f},
            new float[]{17.1917f, 18.5089f, 20.5694f, 22.4650f, 24.7966f, 27.8359f, 29.0540f, 40.2228f, 41.7780f, 46.0588f, 49.8018f, 53.2033f, 57.3420f, 60.2746f},
            new float[]{20.7066f, 22.1642f, 24.4331f, 26.5093f, 29.0505f, 32.3449f, 33.6603f, 45.6160f, 47.2685f, 51.8050f, 55.7585f, 59.3417f, 63.6908f, 66.7660f},
    };

    static float quiValue(int row, int col){
        return (row < 0 || col < 0) ? -1f
            : TABLE[row][col];
    }
}
