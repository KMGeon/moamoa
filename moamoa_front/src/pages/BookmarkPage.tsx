import LoadingHandler from "../components/LoadingHandlerProps.tsx";
import {useGetAllBookmarks} from "../hook/api/useBookmark.ts";
import {Box, Card, CardContent, Grid, Typography} from '@mui/material';
import styled from '@emotion/styled';

const BookmarkCard = styled(Card)`
  transition: transform 0.2s ease-in-out;
  width: 100%;
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  }
`;

const BookmarkContainer = styled(Box)`
  padding: 24px;
  max-width: 800px;
  margin: 0 auto;
`;

export default function BookmarkPage() {
    const {data, isLoading} = useGetAllBookmarks();
    return (
        <LoadingHandler isLoading={isLoading} fallback={<div>Loading...</div>}>
            <BookmarkContainer>
                <Grid container spacing={2} direction="column">
                    {data?.map((bookmark) => (
                        <Grid item xs={12} sx={{fontWeight: 600, fontSize: 10}} key={bookmark.bookmarkUid}>
                            <BookmarkCard>
                                <CardContent>
                                    <Typography variant="subtitle2" component="div" noWrap sx={{ mb: 2, fontWeight: 600, fontSize: 14 }}>
                                        {bookmark.url}
                                    </Typography>
                                    <Typography variant="inherit"  noWrap>
                                        {bookmark.category}
                                    </Typography>
                                    <Typography color="text.secondary" variant="subtitle2" sx={{fontWeight: 600, fontSize: 12}}>
                                        생성일: {bookmark.createdAt}
                                    </Typography>
                                </CardContent>
                            </BookmarkCard>
                        </Grid>
                    ))}
                </Grid>
            </BookmarkContainer>
        </LoadingHandler>
    );
}

