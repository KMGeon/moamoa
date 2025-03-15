import React from 'react';
import { Box, Typography } from '@mui/material';

const RecordPage: React.FC = () => {
    return (
        <Box>
            <Typography variant="h6" gutterBottom>
                기록
            </Typography>
            <Typography variant="body1">
                활동 기록이 여기에 표시됩니다.
            </Typography>
        </Box>
    );
};

export default RecordPage; 