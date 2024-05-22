const BACKEND_URL =
    process.env.REACT_APP_BACKEND_URL || "http://localhost:8090/api/crypto/";

// returns a Promise
function transformToJsonOrTextPromise(response) {
    const contentLength = response.headers.get("Content-Length");
    const contentType = response.headers.get("Content-Type");
    if (
        contentLength !== "0" &&
        contentType &&
        contentType.includes("application/json")
    ) {
        return response.json();
    } else {
        return response.text();
    }
}

async function sendRequest(url, { method = "GET", body, headers = {} }) {
    const options = {
        method,
        headers: new Headers({ "content-type": "application/json", ...headers }),
        body: body ? JSON.stringify(body) : null,
    };

    return fetch(url, options).then((res) => {
        const jsonOrTextPromise = transformToJsonOrTextPromise(res);

        if (res.ok) {
            return jsonOrTextPromise;
        } else {
            return jsonOrTextPromise.then(function (response) {
                const responseObject = {
                    status: res.status,
                    ok: false,
                    message: typeof response === "string" ? response : response.message,
                };

                return Promise.reject(responseObject);
            });
        }
    });
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
export async function createKeyStore(password) {
    return sendRequest(BACKEND_URL + `create-keystore`, {
        method: "POST",
        body: {
            password,
        },
    });
}

export async function generateAESKey(password, keySize, alias) {
    return sendRequest(BACKEND_URL + `generate/aes`, {
        method: "POST",
        body: {
            keySize,
            alias,
            password,
        },
    });
}

export async function getAESKey(password, alias) {
    return sendRequest(BACKEND_URL + `load/aes`, {
        headers: {
            alias,
            password,
        },
    });
}


export async function encryptAES(password, plainText, alias, randomAlgorithm, seed) {
    return sendRequest(BACKEND_URL + `encrypt/aes`, {
        method: "POST",
        body: {
            plainText,
            alias,
            password,
            randomAlgorithm,
            seed,
        },
    });
}


export async function decryotAES(password, cipherText, alias) {
    return sendRequest(BACKEND_URL + `decrypt/aes`, {
        method: "POST",
        body: {
            cipherText,
            alias,
            password,
        },
    });
}

export async function getAliases(password) {
    return sendRequest(BACKEND_URL + `decrypt/aes`, {
        headers: {
            password,
        },
    });
}


export async function generateRSAKey(password, keySize, alias) {
    return sendRequest(BACKEND_URL + `generate/rsa`, {
        method: "POST",
        body: {
            keySize,
            alias,
            password,
        },
    });
}

export async function encryptRSA(password, plainText, alias, randomAlgorithm, seed) {
    return sendRequest(BACKEND_URL + `encrypt/rsa`, {
        method: "POST",
        body: {
            plainText,
            alias,
            password,
            randomAlgorithm,
            seed,
        },
    });
}

export async function decryotRSA(password, cipherText, alias) {
    return sendRequest(BACKEND_URL + `decrypt/rsa`, {
        method: "POST",
        body: {
            cipherText,
            alias,
            password,
        },
    });
}


export async function generateDSAKey(password, keySize, alias) {
    return sendRequest(BACKEND_URL + `generate/dsa`, {
        method: "POST",
        body: {
            keySize,
            alias,
            password,
        },
    });
}


export async function singText(password, plainText, alias, randomAlgorithm, seed) {
    return sendRequest(BACKEND_URL + `sign-text`, {
        method: "POST",
        body: {
            plainText,
            alias,
            password,
            randomAlgorithm,
            seed,
        },
    });
}

export async function verifySignature(password, alias, text, signature) {
    return sendRequest(BACKEND_URL + `verify-text`, {
        method: "POST",
        body: {
            alias,
            password,
            text,
            signature,
        },
    });
}

export async function getRSAPublicKey(password, alias) {
    return sendRequest(BACKEND_URL + `rsa/public`, {
        headers: {
            alias,
            password,
        },
    });
}

export async function getRSAPrivateKey(password, alias) {
    return sendRequest(BACKEND_URL + `rsa/private`, {
        headers: {
            alias,
            password,
        },
    });
}

export async function getFilteredAliases(password, filter) {
    return sendRequest(BACKEND_URL + `filter-aliases`, {
        method: "POST",
        body: {
            password,
            filter,
        },
    });
}
