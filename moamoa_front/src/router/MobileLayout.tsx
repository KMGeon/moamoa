import React from 'react';
import {Avatar, Badge, Box, Container, IconButton, Tab, Tabs} from '@mui/material';
import {styled} from '@mui/material/styles';
import {Outlet, useLocation, useNavigate} from 'react-router-dom';
import {
    Explore as ExploreIcon,
    Home as HomeIcon,
    Message as MessageIcon,
    Notifications as NotificationsIcon,
    Person as PersonIcon
} from '@mui/icons-material';

const OuterContainer = styled(Box)({
    width: '100vw',
    height: '100vh',
    backgroundColor: '#000000',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'stretch',
    margin: 0,
    padding: 0,
    overflow: 'hidden'
});

const MobileContainer = styled(Container)(({theme}) => ({
    width: '100%',
    height: '100%',
    backgroundColor: '#ffffff',
    margin: 0,
    padding: 0,
    overflow: 'hidden',
    display: 'flex',
    flexDirection: 'column',
    position: 'relative',
    transition: 'max-width 0.3s ease',
    // 모바일 우선
    maxWidth: '100% !important',
    // 태블릿
    [theme.breakpoints.up('sm')]: {
        maxWidth: '390px !important',
        boxShadow: theme.shadows[8]
    },
    // 작은 데스크톱
    [theme.breakpoints.up('md')]: {
        maxWidth: '420px !important'
    },
    // 큰 데스크톱
    [theme.breakpoints.up('lg')]: {
        maxWidth: '448px !important'
    }
}));

const Header = styled(Box)(({theme}) => ({
    padding: theme.spacing(0.75, 1.5),
    borderBottom: '1px solid',
    borderColor: theme.palette.divider,
    backgroundColor: 'rgba(255, 255, 255, 0.8)',
    backdropFilter: 'blur(10px)',
    position: 'sticky',
    top: 0,
    zIndex: 1100,
    [theme.breakpoints.up('sm')]: {
        padding: theme.spacing(1, 2),
    }
}));


const MainContent = styled(Box)(({theme}) => ({
    flex: 1,
    overflow: 'auto',
    backgroundColor: '#f8f9fa',
    padding: theme.spacing(2),
    [theme.breakpoints.up('sm')]: {
        padding: theme.spacing(2.5),
    }
}));

const BottomNavigation = styled(Box)(({theme}) => ({
    position: 'sticky',
    bottom: 0,
    backgroundColor: 'rgba(255, 255, 255, 0.8)',
    backdropFilter: 'blur(10px)',
    borderTop: '1px solid',
    borderColor: theme.palette.divider,
    padding: theme.spacing(0.75),
    zIndex: 1100,
    [theme.breakpoints.up('sm')]: {
        padding: theme.spacing(1),
    }
}));


const MobileLayout: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const getActiveTab = () => {
        const path = location.pathname;
        if (path.includes('/bookmark')) return 0;
        if (path.includes('/summary')) return 1;
        if (path.includes('/record')) return 2;
        return 0;
    };

    const handleTabChange = (_: React.SyntheticEvent, newValue: number) => {
        switch (newValue) {
            case 0:
                navigate('/bookmark');
                break;
            case 1:
                navigate('/summary');
                break;
            case 2:
                navigate('/record');
                break;
            default:
                navigate('/bookmark');
        }
    };

    return (
        <OuterContainer>
            <MobileContainer disableGutters>
                {/* Header */}
                <Header>
                    <Box display="flex" alignItems="center" justifyContent="space-between">
                        <Box display="flex" alignItems="center" gap={1}>
                            <Box
                                sx={{
                                    width: 28,
                                    height: 28,
                                    backgroundColor: 'primary.main',
                                    borderRadius: 1,
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    color: 'white',
                                }}
                            >
                                <Box component="span" fontWeight="bold">M</Box>
                            </Box>
                            <Box component="span" fontWeight="bold" fontSize="1.2rem">MoaMoa</Box>
                        </Box>
                        <Box display="flex" alignItems="center" gap={1}>
                            <IconButton size="small">
                                <MessageIcon/>
                            </IconButton>
                            <IconButton size="small">
                                <Badge badgeContent={2} color="primary">
                                    <NotificationsIcon/>
                                </Badge>
                            </IconButton>
                            <Avatar
                                sx={{width: 32, height: 32}}
                                src="https://github.com/shadcn.png"
                            />
                        </Box>
                    </Box>
                </Header>

                <Tabs
                    value={getActiveTab()}
                    onChange={handleTabChange}
                    variant="fullWidth"
                    sx={{
                        borderBottom: 1,
                        borderColor: 'divider',
                        backgroundColor: 'white',
                        '& .MuiTab-root': {
                            minWidth: 'auto',
                        },
                    }}
                >
                    <Tab label="북마크"/>
                    <Tab label="요약"/>
                    <Tab label="기록"/>
                </Tabs>

                <MainContent>
                    <Box p={2}>
                        <Outlet/>
                    </Box>
                </MainContent>

                <BottomNavigation>
                    <Box
                        display="flex"
                        justifyContent="space-around"
                        alignItems="center"
                        py={1}
                    >
                        <Box display="flex" flexDirection="column" alignItems="center">
                            <IconButton size="small" color="primary">
                                <HomeIcon/>
                            </IconButton>
                            <Box fontSize="0.75rem" color="primary.main">홈</Box>
                        </Box>
                        <Box display="flex" flexDirection="column" alignItems="center">
                            <IconButton size="small">
                                <ExploreIcon/>
                            </IconButton>
                            <Box fontSize="0.75rem" color="text.secondary">탐색</Box>
                        </Box>
                        <Box width={48}/> {/* Spacer for FAB */}
                        <Box display="flex" flexDirection="column" alignItems="center">
                            <IconButton size="small">
                                <Badge badgeContent={2} color="primary">
                                    <NotificationsIcon/>
                                </Badge>
                            </IconButton>
                            <Box fontSize="0.75rem" color="text.secondary">알림</Box>
                        </Box>
                        <Box display="flex" flexDirection="column" alignItems="center">
                            <IconButton size="small">
                                <Avatar sx={{width: 24, height: 24}}>
                                    <PersonIcon/>
                                </Avatar>
                            </IconButton>
                            <Box fontSize="0.75rem" color="text.secondary">프로필</Box>
                        </Box>
                    </Box>
                </BottomNavigation>

                <Box sx={{height: 'env(safe-area-inset-bottom)'}}/>
            </MobileContainer>
        </OuterContainer>
    );
};

export default MobileLayout;