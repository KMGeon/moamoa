import React from 'react';
import { Box, Typography } from '@mui/material';

const SummaryPage: React.FC = () => {
    return (
        <Box>
            <Typography variant="h6" gutterBottom>
                요약
            </Typography>
            <Typography variant="body1">
                컨텐츠 요약이 여기에 표시됩니다.
            </Typography>
        </Box>
    );
};

export default SummaryPage; 