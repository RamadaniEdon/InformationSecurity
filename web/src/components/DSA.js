import React, { useState, useRef, useEffect } from 'react';
import { Box, Heading, FormControl, FormLabel, Input, Select, Button, Flex } from '@chakra-ui/react';
import Cryptor from './AESCryptor';
import DSAVerify from './DSAVerify';
import DSASign from './DSASign';

const DSA = () => {
    const [keyAlias, setKeyAlias] = useState('');
    const [keySize, setKeySize] = useState('');
    const [randomnessSource, setRandomnessSource] = useState('');
    const [privateKey, setPrivateKey] = useState('');
    const [publicKey, setPublicKey] = useState('');
    const [selectedKey, setSelectedKey] = useState('');

    const privateDivRef = useRef(null);
    const publicDivRef = useRef(null);

    const autoResizeDiv = (ref) => {
        if (ref.current) {
            ref.current.style.height = 'auto';
            ref.current.style.height = ref.current.scrollHeight + 'px';
        }
    };

    useEffect(() => {
        autoResizeDiv(privateDivRef);
        autoResizeDiv(publicDivRef);
    }, [privateKey, publicKey]);

    const handleGenerateKeys = () => {
        // Logic to generate the keys based on input values
        // For demonstration purposes, simply setting random strings as the keys
        const randomPrivateKey = Math.random().toString(36).substr(2, 8);
        const randomPublicKey = Math.random().toString(36).substr(2, 8);
        setPrivateKey(randomPrivateKey);
        setPublicKey(randomPublicKey);
    };

    const handleSelectKey = (key) => {
        setSelectedKey(key);
        // Logic to handle the selected key
    };

    return (
        <>
            <Box p={8} textAlign="center">
                <Heading as="h1" mb={6}>DSA Encryption</Heading>
                <Box width="80%" margin="0 auto">
                    <Flex justifyContent="space-between">
                        <Box width="48%">
                            <form>
                                <FormControl id="keyAlias" mb={4}>
                                    <FormLabel>Key Alias</FormLabel>
                                    <Input type="text" value={keyAlias} onChange={(e) => setKeyAlias(e.target.value)} />
                                </FormControl>
                                <FormControl id="keySize" mb={4}>
                                    <FormLabel>Key Size</FormLabel>
                                    <Select value={keySize} onChange={(e) => setKeySize(e.target.value)}>
                                        <option value="1024">1024 bits</option>
                                        <option value="2048">2048 bits</option>
                                        <option value="4096">4096 bits</option>
                                    </Select>
                                </FormControl>
                                <FormControl id="randomnessSource" mb={4}>
                                    <FormLabel>Randomness Source</FormLabel>
                                    <Select value={randomnessSource} onChange={(e) => setRandomnessSource(e.target.value)}>
                                        <option value="hardware">Hardware</option>
                                        <option value="software">Software</option>
                                    </Select>
                                </FormControl>
                                <Button colorScheme="teal" onClick={handleGenerateKeys} mb={4} mt={4}>Generate Keys</Button>
                            </form>

                        </Box>
                        <Box width="48%">
                            <FormControl mb={4}>
                                <FormLabel>Select Key</FormLabel>
                                <Select value={selectedKey} onChange={(e) => handleSelectKey(e.target.value)}>
                                    {/* Option values will be populated dynamically */}
                                </Select>
                            </FormControl>
                            <Box>
                                <div
                                    ref={privateDivRef}
                                    contentEditable={false}
                                    style={{
                                        border: '1px solid #CBD5E0',
                                        borderRadius: '0.25rem',
                                        padding: '0.5rem',
                                        minHeight: '7rem',
                                        overflow: 'auto',
                                        resize: 'none',
                                        width: '100%',
                                        color: privateKey ? '#000' : '#CBD5E0',
                                        marginBottom: '1rem'
                                    }}
                                >
                                    {privateKey ? privateKey : 'View the private key here...'}
                                </div>
                                <div
                                    ref={publicDivRef}
                                    contentEditable={false}
                                    style={{
                                        border: '1px solid #CBD5E0',
                                        borderRadius: '0.25rem',
                                        padding: '0.5rem',
                                        minHeight: '7rem',
                                        overflow: 'auto',
                                        resize: 'none',
                                        width: '100%',
                                        color: publicKey ? '#000' : '#CBD5E0'
                                    }}
                                >
                                    {publicKey ? publicKey : 'View the public key here...'}
                                </div>
                            </Box>
                        </Box>
                    </Flex>
                </Box>
            </Box>
            <DSASign />
            <DSAVerify />
        </>
    );
};

export default DSA;
