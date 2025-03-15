import React from 'react';
import { Box, Typography } from '@mui/material';

const BookmarkPage: React.FC = () => {
    return (
        <Box>
            <Typography variant="h6" gutterBottom>
                북마크
            </Typography>
            <Typography variant="body1">
                북마크한 컨텐츠가 여기에 표시됩니다.
            </Typography>
        </Box>
    );
};

export default BookmarkPage; 