import React, { useState, useRef, useEffect } from 'react';
import { Box, Heading, FormControl, FormLabel, Input, Select, Button, Flex } from '@chakra-ui/react';
import Cryptor from './AESCryptor';

const AES = () => {
    const [keyAlias, setKeyAlias] = useState('');
    const [keySize, setKeySize] = useState('');
    const [randomnessSource, setRandomnessSource] = useState('');
    const [generatedKey, setGeneratedKey] = useState('');
    const [selectedKey, setSelectedKey] = useState('');

    // Ref for the div element
    const divRef = useRef(null);

    // Function to dynamically resize the div to fit its content
    const autoResizeDiv = () => {
        if (divRef.current) {
            divRef.current.style.height = 'auto';
            divRef.current.style.height = divRef.current.scrollHeight + 'px';
        }
    };

    // Call autoResizeDiv function whenever the generatedKey changes
    useEffect(() => {
        autoResizeDiv();
    }, [generatedKey]);

    const handleGenerateKey = () => {
        // Logic to generate the key based on input values
        // For demonstration purposes, simply setting a random string as the generated key
        const randomKey = Math.random().toString(36).substr(2, 8);
        setGeneratedKey(randomKey);
    };

    const handleSelectKey = (key) => {
        setSelectedKey(key);
        // Logic to handle the selected key
    };

    return (
        <>
            <Box p={8} textAlign="center">
                <Heading as="h1" mb={6}>AES Encryption</Heading>
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
                                        <option value="128">128 bits</option>
                                        <option value="192">192 bits</option>
                                        <option value="256">256 bits</option>
                                    </Select>
                                </FormControl>
                                <FormControl id="randomnessSource" mb={4}>
                                    <FormLabel>Randomness Source</FormLabel>
                                    <Select value={randomnessSource} onChange={(e) => setRandomnessSource(e.target.value)}>
                                        <option value="hardware">Hardware</option>
                                        <option value="software">Software</option>
                                    </Select>
                                </FormControl>
                                <Button colorScheme="teal" onClick={handleGenerateKey} mb={4} mt={4}>Generate Key</Button>
                            </form>
                        </Box>
                        <Box width="48%">
                            <FormControl mb={4}>
                                <FormLabel>Select Key</FormLabel>
                                <Select value={selectedKey} onChange={(e) => handleSelectKey(e.target.value)}>
                                    {/* Option values will be populated dynamically */}
                                </Select>
                            </FormControl>
                            <div
                                ref={divRef}
                                contentEditable={false} // Disable content editing
                                style={{
                                    border: '1px solid #CBD5E0', // Border color
                                    borderRadius: '0.25rem', // Border radius
                                    padding: '0.5rem', // Padding
                                    minHeight: '14.2rem', // Minimum height
                                    overflow: 'auto', // Enable scrolling if content overflows
                                    resize: 'none', // Disable resizing
                                    width: '100%', // Full width
                                    color: generatedKey ? '#000' : '#CBD5E0', // Text color
                                }}
                            >
                                {generatedKey ? generatedKey : 'View the key here...'}
                            </div>
                        </Box>
                    </Flex>
                </Box>
            </Box>
            <Cryptor />
        </>
    );
};

export default AES;
