import React, { useState } from 'react';
import { Box, Heading, Button, Input, FormControl, FormLabel, VStack } from '@chakra-ui/react';
import { useAuth } from '../context/AuthContext';
import { getKeyStore } from '../services/api';

function Home() {
    const { setToken, isAuthenticated } = useAuth();
    const [password, setPassword] = useState('');

    const handleLogin = () => {
        // For this example, we're just using the password as the token.
        // In a real application, you would verify the password and obtain a token from your authentication service.
        getKeyStore(password).then((token) => {
            setToken(token);
        });
    };

    return (
        <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
            <VStack spacing={4} align="stretch" width="300px">
                <Heading as="h1" textAlign="center">Home Page</Heading>
                <FormControl>
                    <FormLabel htmlFor="password">Key Store Password</FormLabel>
                    <Input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </FormControl>
                <Button colorScheme="teal" size="lg" onClick={handleLogin}>
                    Log In
                </Button>
            </VStack>
        </Box>
    );
}

export default Home;
