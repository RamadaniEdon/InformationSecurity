import React, { useState } from 'react';
import { Box, Heading, FormControl, FormLabel, Input, Select, Button, Flex, Textarea, useToast } from '@chakra-ui/react';

const RSACVerify = () => {
    const [textToVerify, setTextToVerify] = useState('');
    const [signature, setSignature] = useState('');
    const [selectedKey, setSelectedKey] = useState('');
    const toast = useToast();

    const handleUploadText = (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.onload = (e) => setTextToVerify(e.target.result);
        reader.readAsText(file);
    };

    const handleUploadSignature = (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.onload = (e) => setSignature(e.target.result);
        reader.readAsText(file);
    };

    const handleVerify = () => {
        // Replace with actual verification logic
        const isVerified = Math.random() > 0.5;

        toast({
            title: isVerified ? 'Verification Successful' : 'Verification Failed',
            description: isVerified ? 'The signature is verified.' : 'The signature could not be verified.',
            status: isVerified ? 'success' : 'error',
            duration: 5000,
            isClosable: true,
        });
    };

    return (
        <Box p={8} textAlign="center">
            <Heading as="h1" mb={6}>Signature Verification</Heading>
            <Box width="80%" margin="0 auto">
                <Flex justifyContent="space-between" mb={4}>
                    <Box width="48%">
                        <FormControl id="textToVerify" mb={4}>
                            <FormLabel>Text to Verify</FormLabel>
                            <Textarea 
                                value={textToVerify}
                                onChange={(e) => setTextToVerify(e.target.value)}
                                placeholder="Enter the text to verify..."
                                size="md"
                                resize="vertical"
                            />
                        </FormControl>
                        <Button as="label" htmlFor="upload-text" colorScheme="teal" mb={4}>
                            Upload Text
                        </Button>
                        <Input type="file" id="upload-text" display="none" onChange={handleUploadText} />
                    </Box>
                    <Box width="48%">
                        <FormControl id="signature" mb={4}>
                            <FormLabel>Signature</FormLabel>
                            <Textarea
                                value={signature}
                                onChange={(e) => setSignature(e.target.value)}
                                placeholder="Enter the signature..."
                                size="md"
                                resize="vertical"
                            />
                        </FormControl>
                        <Button as="label" htmlFor="upload-signature" colorScheme="teal" mb={4}>
                            Upload Signature
                        </Button>
                        <Input type="file" id="upload-signature" display="none" onChange={handleUploadSignature} />
                    </Box>
                </Flex>
                <FormControl mb={4}>
                    <FormLabel>Select Key</FormLabel>
                    <Select value={selectedKey} onChange={(e) => setSelectedKey(e.target.value)}>
                        {/* Populate with available keys */}
                        <option value="key1">Key 1</option>
                        <option value="key2">Key 2</option>
                    </Select>
                </FormControl>
                <Button colorScheme="teal" onClick={handleVerify} mb={4}>Verify</Button>
            </Box>
        </Box>
    );
};

export default RSACVerify;
