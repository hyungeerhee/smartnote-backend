## 🧠 AI 기능 (OpenAI GPT 기반)

Smart Note는 단순한 노트 애플리케이션을 넘어, OpenAI GPT 모델을 활용한 인공지능 기능을 통해 사용자의 정보 처리 능력을 크게 향상시킵니다.

### 🔹 자연어 기반 요약 (Abstractive Summarization)
- GPT-3.5-turbo 모델을 활용하여 메모의 핵심 내용을 요약합니다.
- 단순 문장 추출이 아닌, 의미 중심의 자연어 생성 기반 요약을 제공합니다.
- 긴 회의록, 강의 필기, 업무 메모 등을 빠르게 정리할 수 있도록 지원합니다.

### 🔹 메모 기반 질의응답 (Contextual Q&A)
- 메모 내용을 기반으로 GPT에 질문을 전달하여, 의미적 응답을 생성합니다.
- 예: "이 메모에서 주요 개념은 무엇인가요?", "이 내용을 3줄 요약해줘"
- GPT는 전체 메모 문맥을 이해하고, 요약 또는 재구성된 답변을 생성합니다.

### 🔹 다국어 번역 (Multilingual Translation)
- 지정된 언어 코드(`lang: "en"`, `"ja"` 등)를 통해 메모 내용을 자동 번역합니다.
- GPT의 고품질 언어 모델을 통해, 단순 직역이 아닌 자연스러운 번역을 제공합니다.
- 글로벌 사용자, 해외 협업 또는 다국어 문서 관리에 유용합니다.

### 🔹 AI 맞춤법 및 문장 교정 (Proofreading)
- GPT를 통해 문법 오류, 띄어쓰기, 어색한 표현 등을 감지하고 수정 제안을 반환합니다.
- 전통적인 맞춤법 검사기 대비 문맥 인식 정확도가 높습니다.
- 글쓰기 품질 개선, 보고서 교정 등에 효과적입니다.

—

## ⚙️ AI 요청 구조

- 모든 AI 기능은 인증된 사용자만 접근 가능 (JWT 기반 인증)
- API Endpoint 구조:
  - `POST /api/notes/{id}/summary` – 요약 요청
  - `POST /api/notes/{id}/ask` – 질문 요청
  - `POST /api/notes/{id}/translate` – 번역 요청
  - `POST /api/notes/{id}/spellcheck` – 맞춤법 검사
- 내부적으로 `RestTemplate`을 통해 OpenAI API에 HTTP 요청을 전송
- `application.properties` 또는 `application.yml`에서 API Key 및 URL을 외부 설정으로 관리

---

## 📌 사용 모델 및 요금

- 사용 모델: `gpt-3.5-turbo`
- 응답은 4096 tokens 제한 내에서 처리
- 사용량 및 비용은 OpenAI 과금 정책에 따라 청구 
- 예: 요약 1회 요청 시 평균 500~700 tokens 사용
