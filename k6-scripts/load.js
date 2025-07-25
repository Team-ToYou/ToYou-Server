import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '1m', target: 1000 }
    ],
};

const BASE_URL = 'http://localhost:8080';
const TOKEN = __ENV.K6_TOKEN;

const params = {
    headers: {
        Authorization: `Bearer ${TOKEN}`,
        'Content-Type': 'application/json',
    },
};

export default function () {
    const min = 247753;
    const max = 407752;
    const targetId = Math.floor(Math.random() * (max - min + 1)) + min;

    const payload = JSON.stringify({
        "targetId": targetId,
        "content": "content",
        "questionType": "SHORT_ANSWER",
        "anonymous": false
    });

    const res = http.post(`${BASE_URL}/questions`, payload, params);

    check(res, {
        'status is 2xx': (r) => r.status >= 200 && r.status < 300,
        'response time < 300ms': (r) => r.timings.duration < 300,
    });
    sleep(1);
}
