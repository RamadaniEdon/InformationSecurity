export const greenToast = (toast, message = "Operation Successful") => {
    toast({
        title: "Success",
        description: message,
        status: "success",
        duration: 5000,
        isClosable: true,
    });
}

export const redToast = (toast, message = "Operation failed") => {
    toast({
        title: "Error",
        description: message,
        status: "error",
        duration: 5000,
        isClosable: true,
    });
}

export const yellowToast = (toast, message = "Operation needs attention") => {
    toast({
        title: "Warning",
        description: message,
        status: "warning", // Set the status to "warning" for a yellowish or orange color
        duration: 5000,
        isClosable: true,
    });
}