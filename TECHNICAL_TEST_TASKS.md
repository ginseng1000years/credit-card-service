# Credit Card Service - Technical Test Tasks for Fresher/Junior Developers

**Mục tiêu**: Kiểm tra kỹ năng lập trình thực tế, problem-solving, và khả năng làm việc độc lập của candidates ở level Junior.

**Yêu cầu chung**:
- Fork project này
- Mỗi task là một feature/improvement riêng
- Commit message phải rõ ràng và follow convention
- Viết unit tests cho tất cả feature mới
- Update documentation nếu cần

---

## 🎯 TASK SELECTION GUIDE

**Chọn 1-3 tickets dựa vào tier:**

### **TIER 1: Junior Entry-Level** ⭐⭐ (2-4 hours)
*Fresher vừa ra trường hoặc < 1 năm kinh nghiệm*

👉 **Recommend chọn 1 ticket**:
- **#14**: Swagger/OpenAPI Documentation (Easy - **START HERE**)
- **#1**: Refund Transaction Feature (Medium)
- **#3**: Pagination & Filtering (Medium)

**Expected**: Basic to medium code quality, good testing

---

### **TIER 2: Junior with Some Experience** ⭐⭐⭐ (3-5 hours)
*Fresher có vài tháng kinh nghiệm hoặc strong college project*

👉 **Recommend chọn 1-2 tickets**:
- **#2**: Custom Validation Annotations (Medium)*
- **#13**: Transaction Fee Calculation (Medium)
- **#10**: Error Handling & Recovery (Medium)*

**Expected**: Solid design, comprehensive tests, edge cases

---

### **TIER 3: Stretch Goals** ⭐⭐⭐⭐ (5-7 hours)
*Filter top 10% candidates - challenges to think differently*

👉 **Recommend chọn 1 ticket** (optional, để identify top performers):
- **#6**: Concurrent Transaction Handling (Hard)
- **#7**: Card Spending Limits (Hard)*
- **#12**: Transaction Settlement (Hard)
- **#15**: Database Performance (Hard)
- **#11**: Caching Layer (Medium-Hard)*
- **#8**: Database Migration (Medium-Hard)*
- **#5**: Transaction Status History (Hard)*
- **#4**: Audit Logging (Hard)*
- **#9**: API Rate Limiting (Hard)*
- **#16**: Multi-Currency Support (Hard)*

**Expected**: Advanced thinking, system design, optimization

---

**SCORING NOTE**: 
- Pass (Tier 1): >= 70/100 points
- Pass (Tier 2): >= 75/100 points  
- Pass (Tier 3): >= 80/100 points

---

## 🎯 Ticket #1: Implement Refund Transaction Feature
**Tier**: ⭐⭐ JUNIOR ENTRY-LEVEL
**Level**: Medium
**Estimated Time**: 4-6 hours

### 🎯 Why This Ticket?
**Business Context**: 
- Refund là một trong những features CRITICAL trong bất kỳ payment system nào
- Customers muốn refund transactions nếu có issues
- Bank/merchant cần track refunds cho reconciliation
- Refund logic phức tạp hơn authorize (có validation, state management)

**What You'll Learn**:
- ✅ Extend existing feature (real-world scenario)
- ✅ Understand state machine (AUTHORIZED → CAPTURED → REFUNDED)
- ✅ Handle money calculations safely (BigDecimal)
- ✅ Implement business validation rules
- ✅ Write comprehensive tests for edge cases

**Impact**:
- Refund is used by millions of transactions daily
- Better refund system = happier customers
- Security & correctness critical (real money!)

### Mô tả:
Thêm chức năng hoàn tiền (refund) cho các giao dịch đã capture.

### Yêu cầu:
1. Tạo endpoint `POST /transactions/refund/{transactionId}` với các rule:
   - Chỉ có thể refund transaction đang ở state CAPTURED
   - Refund amount không được vượt quá original transaction amount
   - Support partial refund (refund một phần)
   - Khi refund, phải cập nhật available limit trở lại
   - Tạo refund transaction record mới với state REFUNDED

2. Update TransactionType enum để thêm REFUNDED state

3. Update CardTransaction entity nếu cần để track refund amount

4. Viết ít nhất 8 unit tests để cover:
   - Refund thành công (full refund)
   - Refund một phần (partial refund)
   - Refund với amount vượt quá original amount (should fail)
   - Refund transaction không ở state CAPTURED (should fail)
   - Available limit được cập nhật đúng

### Acceptance Criteria:
- ✅ API endpoint hoạt động đúng
- ✅ Business logic xử lý đúng các edge case
- ✅ Available limit được restore đúng
- ✅ 8+ unit tests passing
- ✅ Code coverage >= 80%

### 💡 Tips for Fresher:
- Study existing `authorizeTransaction` flow để understand pattern
- Validation logic tương tự như authorize
- Use Mockito để mock CardService & TransactionRepository

### 🔍 What Interviewers Look For:
- Can you understand existing code?
- Can you extend existing patterns?
- Do you handle all edge cases?
- Are tests comprehensive?

---

## 🎯 Ticket #2: Add Validation Layer with Custom Annotations
**Tier**: ⭐⭐⭐ JUNIOR+
**Level**: Medium*
**Estimated Time**: 3-5 hours

### 🎯 Why This Ticket?
**Business Context**:
- Bad data = bad decisions (garbage in, garbage out)
- Validation is first line of defense against invalid requests
- Custom validation annotations = professional, reusable code
- Server-side validation is CRITICAL (client validation not enough!)

**What You'll Learn**:
- ✅ Spring validation framework (most teams use it)
- ✅ Custom annotations (reusable, DRY principle)
- ✅ Validation messages (user-friendly errors)
- ✅ Business rule enforcement (e.g., card number format)
- ✅ Integration testing with invalid data

**Impact**:
- Prevents invalid data from entering system
- Better error messages for API consumers
- Reusable validation across codebase
- Professional code quality

### Mô tả:
Tạo custom validation annotations để validate request parameters thay vì hard-code logic trong service.

### Yêu cầu:
1. Tạo custom annotation `@ValidCreditCardNumber` để validate card number format:
   - Card number phải là 16 digits
   - Luhn algorithm: **OPTIONAL** (+1 điểm bonus)

2. Tạo custom annotation `@ValidAmount` để validate transaction amount:
   - Amount phải > 0
   - Amount không vượt quá 999,999.99
   - Support BigDecimal

3. Tạo custom annotation `@ValidCardId` để validate card ID:
   - Card ID phải > 0
   - Card phải tồn tại trong database

4. Apply các annotations vào AuthorizeRequest và CreditCard entities

5. Viết validation tests để verify các annotations hoạt động đúng

### Acceptance Criteria:
- ✅ Custom annotations được implement
- ✅ Annotations được apply đúng vào entities
- ✅ Validation error messages rõ ràng
- ✅ 10+ validation tests passing
- ✅ Integration tests verify endpoints reject invalid input

### 💡 Tips for Fresher:
- Study Spring's `ConstraintValidator` interface
- Example: Implement `@ValidAmount`:
  ```java
  public class AmountValidator implements ConstraintValidator<ValidAmount, BigDecimal> {
      public boolean isValid(BigDecimal value, ...) {
          // implement logic here
      }
  }
  ```
- Focus on @ValidAmount first, others are similar
- Luhn algorithm là bonus, skip nếu khó

### 🔍 What Interviewers Look For:
- Do you understand Spring validation framework?
- Can you implement ConstraintValidator?
- Are edge cases handled?
- Is error message helpful?

### ⚠️ MODIFICATION for Fresher:
- **Luhn algorithm is OPTIONAL** (don't require for baseline pass)
- **Provide ConstraintValidator template** nếu candidate struggle
- Focus on @ValidAmount as baseline requirement

---

## 🎯 Ticket #3: Implement Pagination and Filtering for Transactions
**Tier**: ⭐⭐ JUNIOR ENTRY-LEVEL
**Level**: Medium
**Estimated Time**: 4-5 hours

### 🎯 Why This Ticket?
**Business Context**:
- Real data = millions of transactions per card
- Can't return ALL transactions (memory issues, slow)
- Pagination = standard REST practice (Google, GitHub, Twitter all use it)
- Filtering = users want to find specific transactions

**What You'll Learn**:
- ✅ Pagination best practices (page, size, hasNext)
- ✅ Spring Data JPA Pageable (most teams use it)
- ✅ Custom @Query writing (SQL & JPQL)
- ✅ Sorting & filtering logic
- ✅ Edge case handling (invalid page, empty results)

**Impact**:
- Better user experience (don't wait for million records)
- More efficient API (smaller responses)
- Professional REST API design
- Foundation for all list endpoints

### Mô tả:
Thêm endpoint để list transactions của một card với pagination và filtering.

### Yêu cầu:
1. Tạo endpoint `GET /cards/{cardId}/transactions` với support:
   - **Pagination**: page, size parameters
   - **Filtering**: by transaction type (AUTHORIZED, CAPTURED, REFUNDED)
   - **Sorting**: by createdAt (ascending/descending), by amount
   - **Date range filtering**: từ date đến date (optional)

2. Response format:
   ```json
   {
     "content": [...],
     "totalElements": 100,
     "totalPages": 5,
     "currentPage": 0,
     "pageSize": 20,
     "hasNext": true,
     "hasPrevious": false
   }
   ```

3. Update TransactionRepository với custom queries using @Query

4. Viết integration tests để test:
   - Pagination hoạt động đúng
   - Filtering theo type hoạt động
   - Sorting theo field khác nhau
   - Date range filtering

### Acceptance Criteria:
- ✅ Endpoint hoạt động với pagination
- ✅ Filtering và sorting đúng
- ✅ Response format consistent
- ✅ 10+ integration tests passing
- ✅ Handle edge cases (empty page, invalid page number)

### 💡 Tips for Fresher:
- Use Spring Data's `PageRequest` & `Pageable`
- Spring handles pagination automatically
- Custom filtering: use `@Query` with `JPQL` or `Specification`
- Test with multiple pages & filter conditions

### 🔍 What Interviewers Look For:
- Do you know Spring Data JPA pagination?
- Can you write custom @Query?
- Do you handle edge cases?
- Are tests comprehensive?

---

## 🎯 Ticket #4: Add Audit Logging for Sensitive Operations
**Tier**: ⭐⭐⭐⭐ STRETCH GOAL
**Level**: Hard*
**Estimated Time**: 3-4 hours

### 🎯 Why This Ticket?
**Business Context**:
- Compliance & audit trail (banks MUST track who did what)
- Regulatory requirement (PCI-DSS, SOX, etc.)
- Fraud detection (find suspicious patterns)
- Debugging (what went wrong and when)
- Legal protection (prove actions were logged)

**What You'll Learn**:
- ✅ Audit logging patterns (real-world requirement)
- ✅ Sensitive data handling (PII protection)
- ✅ Timestamp accuracy (important for audit)
- ✅ Service layer extension (clean architecture)
- ✅ Performance consideration (logging overhead)

**Impact**:
- Makes system compliant with regulations
- Helps detect fraud early
- Debugging becomes much easier
- Legal protection for company

### ⚠️ SIMPLIFIED for Fresher:
Instead of AOP (Aspect-Oriented Programming), use **manual logging approach** - simpler, more understandable.

### Yêu cầu:
1. Tạo AuditLog entity với các fields:
   - id, operationType (AUTHORIZE, CAPTURE, REFUND), cardId, transactionId
   - amount, operator (user/system), timestamp
   - ipAddress (optional), userAgent (optional)

2. Tạo AuditLogRepository và AuditLogService

3. **MANUAL Logging Approach** (NOT AOP):
   - Call `auditLogService.log(...)` trong mỗi operation
   - Simple, easy to understand, easy to debug
   - Example: `auditLogService.logAuthorize(cardId, amount);`

4. Tạo endpoint `GET /audit-logs` (admin only endpoint):
   - List tất cả audit logs
   - Filter by cardId, operationType
   - Sort by timestamp

5. Viết 6+ unit tests

### Acceptance Criteria:
- ✅ Audit logs được tạo khi authorize/capture/refund
- ✅ Audit log service hoạt động đúng
- ✅ Audit log endpoint hoạt động
- ✅ 6+ tests passing
- ✅ Sensitive data không bị log (hoặc masked)

### 💡 Tips for Fresher:
- **DON'T use AOP** - use simple service method calls instead
- Example:
  ```java
  // In TransactionService
  public CardTransaction authorize(...) {
      // do work
      auditLogService.log("AUTHORIZE", cardId, amount);
      return ...;
  }
  ```
- Much easier to understand & debug than AOP
- Still achieves goal of audit logging

### 🔍 What Interviewers Look For:
- Can you design audit log entity?
- Can you extend existing service?
- Do you handle edge cases?
- Is sensitive data protected?

### ⚠️ MODIFICATION for Fresher:
- **Skip AOP completely** - use simple approach
- **Focus on manual logging** in service layer
- AOP is optional bonus (not required)

---

## 🎯 Ticket #5: Implement Transaction Status History
**Tier**: ⭐⭐⭐⭐ STRETCH GOAL
**Level**: Hard*
**Estimated Time**: 5-6 hours

### 🎯 Why This Ticket?
**Business Context**:
- Users want to know "when did my transaction change status?"
- Support team needs history to investigate issues
- Better reconciliation (match with external systems)
- Immutable audit trail (can't just change status silently)
- Trend analysis (how long do transactions take to settle?)

**What You'll Learn**:
- ✅ Event sourcing concept (track state changes, not just current state)
- ✅ Immutable data design (history can't be changed)
- ✅ Timestamp precision (when exactly did change happen)
- ✅ Reason tracking (WHY did it change)
- ✅ Data integrity (prevent inconsistent states)

**Impact**:
- Better user communication
- Easier support/debugging
- Audit trail compliance
- Better analytics & reporting

### ⚠️ SIMPLIFIED for Fresher:
Instead of using JPA lifecycle callbacks (@PreUpdate, @PostUpdate), use **manual history tracking** in service layer - much simpler!

### Yêu cầu:
1. Tạo TransactionStatusHistory entity với:
   - transactionId, previousStatus, newStatus, changedAt, changedReason

2. **MANUAL History Tracking** (NOT Lifecycle Callbacks):
   - Create helper method: `recordStatusChange(transactionId, fromStatus, toStatus, reason)`
   - Call this method when status changes
   - Simple, easy to understand, easy to test
   - Example:
     ```java
     public void captureTransaction(Long txId) {
         CardTransaction tx = getTransaction(txId);
         TransactionType oldStatus = tx.getType();
         
         tx.setType(TransactionType.CAPTURED);
         transactionRepository.save(tx);
         
         historyService.recordStatusChange(txId, oldStatus, CAPTURED, "Captured by user");
     }
     ```

3. Tạo endpoint `GET /transactions/{transactionId}/history`:
   - Return list tất cả status changes của transaction
   - Include timestamp và reason

4. Thêm `changedReason` parameter vào capture/refund endpoints

5. Viết 8+ tests để cover:
   - Status changes được recorded
   - History endpoint trả về đúng data
   - Multiple status changes được track
   - Timestamp accuracy

### Acceptance Criteria:
- ✅ Status history được track
- ✅ History endpoint hoạt động
- ✅ Data integrity được maintain
- ✅ 8+ tests passing
- ✅ Timestamps chính xác

### 💡 Tips for Fresher:
- **Forget about @PreUpdate/@PostUpdate** - use simple approach
- Call `historyService.recordStatusChange()` when you change status
- Much easier to test & understand
- Lifecycle callbacks are advanced topic - skip for now

### 🔍 What Interviewers Look For:
- Can you design history entity?
- Do you understand immutability of history?
- Can you add reason tracking?
- Are tests comprehensive?

### ⚠️ MODIFICATION for Fresher:
- **Skip JPA lifecycle callbacks** - use manual approach
- **Focus on simple history recording** in service
- Lifecycle callbacks optional bonus

---

## 🎯 Ticket #6: Add Concurrent Transaction Handling
**Tier**: ⭐⭐⭐⭐ STRETCH GOAL
**Level**: Hard
**Estimated Time**: 5-7 hours

### 🎯 Why This Ticket?
**Business Context**:
- Mobile users may click "authorize" multiple times by accident
- Network issues might cause duplicate requests
- Multiple payment gateways might send requests simultaneously
- Race conditions = money bugs (very expensive!)
- Real-world: millions of concurrent transactions per second

**What You'll Learn**:
- ✅ Race condition detection (hard to find, easy to create bugs!)
- ✅ Optimistic locking (lightweight, most databases support)
- ✅ Retry logic (handle transient failures gracefully)
- ✅ Concurrency testing (stress test with many threads)
- ✅ Data consistency (prevent double-charging)

**Impact**:
- Prevents double-charging customers (critical!)
- Better system reliability under load
- Professional-grade transaction handling
- Differentiates mediocre from excellent developers

### Mô tả:
Xử lý race condition khi có multiple concurrent requests authorize transaction.

### Yêu cầu:
1. Implement optimistic locking using `@Version` annotation
   - Add version field vào CreditCard entity
   - Handle OptimisticLockingFailureException

2. Thêm pessimistic locking option (optional):
   - Use @Lock(LockModeType.PESSIMISTIC_WRITE) khi cần

3. Implement retry logic:
   - Retry authorize khi có version conflict
   - Max 3 retries với exponential backoff

4. Viết stress tests:
   - 100 concurrent authorize requests trên 1 card
   - Verify available limit không bị negative
   - Verify transaction count đúng

5. Viết 10+ unit/integration tests

### Acceptance Criteria:
- ✅ Optimistic locking implemented
- ✅ Retry logic hoạt động
- ✅ Stress test passing
- ✅ No race condition detected
- ✅ 10+ tests passing

### 💡 Tips for Fresher:
- Optimistic locking: @Version field + JPA handles it
- When conflict happens: catch OptimisticLockingFailureException
- Retry logic: loop with try-catch
- Stress test: use ExecutorService with 100 threads
- This is HARD - don't underestimate!

### 🔍 What Interviewers Look For:
- Do you understand race conditions?
- Can you implement optimistic locking?
- Can you design retry logic?
- Do you think about thread safety?
- Are stress tests comprehensive?

### ⚠️ This is a STRETCH GOAL
- Only attempt if done with Tier 1-2 tickets
- Advanced topic for differentiating top candidates

---

## 🎯 Ticket #7: Implement Card Spending Limits with Time Windows
**Tier**: ⭐⭐⭐⭐ STRETCH GOAL
**Level**: Hard*
**Estimated Time**: 5-6 hours

### 🎯 Why This Ticket?
**Business Context**:
- Banks want to limit fraud (e.g., max $500/day)
- Cards have different limits (student card vs premium card)
- Limits reset on specific schedule (midnight, month-end)
- Compliance requirement (PCI-DSS, KYC rules)
- Risk management (detect unusual spending patterns)

**What You'll Learn**:
- ✅ Scheduled task patterns (@Scheduled)
- ✅ Time window logic (daily, monthly resets)
- ✅ Business rule enforcement (prevent over-limit)
- ✅ Configuration externalization (flexible limits)
- ✅ Edge case handling (exactly at limit, month-end, etc)

**Impact**:
- Reduces fraud losses significantly
- Improves regulatory compliance
- Better risk management
- Customizable per card type

### ⚠️ SIMPLIFIED for Fresher:
- **NO timezone handling** - use server timezone only
- **Implement DAILY limit only** (skip monthly) - easier
- Provide @Scheduled template code

### Mô tả:
Thêm chức năng giới hạn chi tiêu trong một khoảng thời gian (daily, monthly).

### Yêu cầu:
1. Extend CreditCard entity với:
   - dailySpendLimit, dailySpentAmount, lastResetDate

2. Thêm business logic trong authorizeTransaction:
   - Check daily limit trước khi authorize
   - Reset daily amount khi hết thời gian (midnight)
   - Reject if would exceed limit

3. Implement scheduled task để reset limits:
   - Daily reset lúc midnight (00:00)
   - Use `@Scheduled(cron = "0 0 0 * * *")`

4. Tạo endpoint để:
   - Update daily limit
   - Get current spending info

5. Viết 8+ tests:
   - Daily limit validation
   - Auto-reset functionality
   - Same-day transactions
   - Edge case: exactly at limit

### Acceptance Criteria:
- ✅ Daily limit enforced
- ✅ Auto-reset hoạt động
- ✅ Scheduled task chạy đúng
- ✅ 8+ tests passing

### 💡 Tips for Fresher:
- Start with DAILY LIMIT only (skip monthly)
- Don't worry about timezone
- `@Scheduled(cron = "0 0 0 * * *")` = every day at midnight
- LocalDateTime.now() = current time
- LocalDate.now() = current date for reset comparison

### 🔍 What Interviewers Look For:
- Can you understand scheduled tasks?
- Do you handle time correctly?
- Can you design spending limit logic?
- Are edge cases covered?

### ⚠️ MODIFICATIONS for Fresher:
- **Daily only** (skip monthly)
- **No timezone handling**
- **Provide @Scheduled template**

---

## 🎯 Ticket #8: Add Database Migration with Liquibase/Flyway
**Tier**: ⭐⭐⭐⭐ STRETCH GOAL
**Level**: Medium-Hard*
**Estimated Time**: 3-4 hours

### 🎯 Why This Ticket?
**Business Context**:
- Hibernate auto-DDL is for dev only (not production-safe)
- Production = need version control for schema changes
- Schema changes must be reversible (rollback capability)
- Multiple environments (dev, staging, prod) need consistent schema
- Data migration often needed with schema changes

**What You'll Learn**:
- ✅ Database version control (like git for database)
- ✅ Migration tool patterns (Flyway/Liquibase)
- ✅ Forward & backward compatibility
- ✅ Production deployment safety
- ✅ Team coordination (schema changes must be tracked)

**Impact**:
- Production-ready deployment
- Safer schema upgrades (can rollback)
- Better team coordination
- Prevents schema drift between environments

### ⚠️ SIMPLIFIED for Fresher:
- Provide migration file templates
- Focus on basic SQL (CREATE TABLE, ADD COLUMN)
- Skip complex rollback logic

### Mô tả:
Migration từ Hibernate auto-DDL sang proper migration tool (Liquibase hoặc Flyway).

### Yêu cầu:
1. Thêm Liquibase hoặc Flyway dependency vào pom.xml
   - Recommend: **Flyway** (simpler for fresher)

2. Tạo migration files cho:
   - Initial schema creation (tables, columns, indexes)
   - Sample data insertion

3. Update application.yml:
   - Disable Hibernate auto-DDL: `ddl-auto: validate`
   - Enable Flyway: already auto-detected

4. Create migration for new features:
   - Add column, create table, create index

5. Test migrations:
   - Fresh database từ migrations
   - Verify schema matches entities

### Acceptance Criteria:
- ✅ Migration tool configured
- ✅ Initial migration executable
- ✅ Hibernate ddl-auto = validate
- ✅ Schema matches entities
- ✅ Documentation updated

### 💡 Tips for Fresher:
- **Use Flyway (simpler than Liquibase)**
- Migration files go to: `src/main/resources/db/migration/`
- Naming: `V1__initial_schema.sql`, `V2__add_audit_table.sql`
- Just write SQL - Flyway handles versioning
- Each migration file runs once, in version order

### 🔍 What Interviewers Look For:
- Do you understand database versioning?
- Can you write SQL migrations?
- Do you understand Hibernate validation mode?
- Is schema correct?

### ⚠️ MODIFICATIONS for Fresher:
- **Use Flyway (not Liquibase)**
- **Provide migration file templates**
- **Focus on basic SQL only**
- **Skip complex rollback**

---

## 🎯 Ticket #9: Implement API Rate Limiting and Request Throttling
**Level**: ⭐⭐⭐⭐ (Hard)
**Estimated Time**: 4-5 hours

### Mô tả:
Thêm rate limiting để prevent abuse và protect API.

### Yêu cầu:
## 🎯 Ticket #9: Implement API Rate Limiting and Request Throttling
**Tier**: ⭐⭐⭐⭐ STRETCH GOAL
**Level**: Hard*
**Estimated Time**: 4-5 hours

### 🎯 Why This Ticket?
**Business Context**:
- Protection against abuse (DDoS, scraping, brute force)
- Fair resource usage (prevent one user hogging all bandwidth)
- Cost control (limit API calls = limit infrastructure cost)
- Service stability (prevent overload crashes)
- SLA enforcement (premium users get higher limits)

**What You'll Learn**:
- ✅ Rate limiting algorithms (token bucket, sliding window)
- ✅ HTTP headers for rate limit info
- ✅ Per-IP tracking (identify bad actors)
- ✅ Graceful degradation (reject politely)
- ✅ Monitoring (track rate limit violations)

**Impact**:
- Protects API from abuse
- Better service stability
- Cost optimization
- Better user communication (Retry-After header)

### ⚠️ SIMPLIFIED for Fresher:
- Use **simple in-memory solution** (not Redis)
- Implement **per-IP rate limiting only** (not per-API-key)
- Provide template for rate limiting filter

### Mô tả:
Thêm rate limiting để prevent abuse và protect API.

### Yêu cầu:
1. Implement rate limiting:
   - **100 requests/minute per IP** (or provide template)
   - Use simple in-memory approach with Bucket4j or custom
   - Alternative: provide RateLimitFilter template

2. Return appropriate HTTP headers:
   - X-RateLimit-Limit
   - X-RateLimit-Remaining
   - X-RateLimit-Reset
   - Retry-After (when exceeded)

3. Handle rate limit exceeded:
   - Return 429 Too Many Requests
   - Include retry information

4. Viết 6+ tests:
   - Rate limit enforcement
   - Headers returned correctly
   - 429 returned when exceeded

### Acceptance Criteria:
- ✅ Rate limiting implemented
- ✅ Correct headers returned
- ✅ 429 returned when exceeded
- ✅ 6+ tests passing
- ✅ Configuration externalized

### 💡 Tips for Fresher:
- **Use simple approach (not Redis)**
- Option 1: Use Bucket4j library
- Option 2: Custom filter with ConcurrentHashMap tracking
- Track by IP address: request.getRemoteAddr()
- Much simpler than using Redis

### 🔍 What Interviewers Look For:
- Can you implement rate limiting?
- Do you understand HTTP headers?
- Can you write custom filter?
- Are tests comprehensive?

### ⚠️ MODIFICATIONS for Fresher:
- **Simple in-memory (not Redis)**
- **Per-IP only (not per-key)**
- **Provide template if using Bucket4j**

---

## 🎯 Ticket #10: Add Comprehensive Error Handling and Recovery
**Tier**: ⭐⭐⭐ JUNIOR+
**Level**: Medium*
**Estimated Time**: 4-5 hours

### 🎯 Why This Ticket?
**Business Context**:
- Bad error messages = frustrated customers + hard to debug
- Generic "500 Internal Server Error" = not helpful
- Proper errors = better API experience
- Error codes = machine-readable (can retry automatically)
- Structured errors = easier monitoring & debugging

**What You'll Learn**:
- ✅ Custom exception design (meaningful error types)
- ✅ Centralized error handling (DRY - Don't Repeat Yourself)
- ✅ HTTP status code semantics (404 vs 400 vs 500)
- ✅ Structured error responses (machine + human readable)
- ✅ Error tracing (trace ID for debugging)

**Impact**:
- Better API consumer experience
- Easier debugging (trace IDs)
- Professional error handling
- Reduced support tickets

### ⚠️ SIMPLIFIED for Fresher:
- **Skip circuit breaker pattern** (advanced topic)
- Focus on **custom exceptions + proper HTTP codes**
- Simple structured error responses

### Mô tả:
Improve GlobalExceptionHandler với structured error responses dan recovery mechanisms.

### Yêu cầu:
1. Create ErrorResponse DTO:
   ```json
   {
     "errorCode": "INSUFFICIENT_BALANCE",
     "message": "...",
     "details": {...},
     "timestamp": "...",
     "path": "...",
     "traceId": "..."
   }
   ```

2. Tạo custom exceptions:
   - InsufficientBalanceException
   - InvalidTransactionStateException
   - CardNotFoundException
   - etc.

3. Update GlobalExceptionHandler để handle:
   - Custom exceptions → proper HTTP codes
   - Database exceptions → 500 Internal Server Error
   - Validation exceptions → 400 Bad Request
   - Unknown exceptions → 500 with generic message

4. **SKIP circuit breaker** (not required for fresher)

5. Viết 10+ tests

### Acceptance Criteria:
- ✅ Custom exceptions implemented
- ✅ All exceptions handled properly
- ✅ Error responses structured
- ✅ Error codes consistent
- ✅ Trace ID for debugging
- ✅ 10+ tests passing

### 💡 Tips for Fresher:
- **Don't implement circuit breaker** - too advanced
- Focus on custom exceptions + handler
- Each custom exception → specific HTTP code
- Use traceId: UUID.randomUUID().toString()
- Test different exception scenarios

### 🔍 What Interviewers Look For:
- Can you design custom exceptions?
- Do you understand HTTP status codes?
- Can you write centralized error handling?
- Are error messages helpful?

### ⚠️ MODIFICATIONS for Fresher:
- **Skip circuit breaker completely**
- **Focus on basics: custom exceptions + handler**

---

2. Tạo custom exceptions:
   - InsufficientBalanceException
   - InvalidTransactionStateException
   - CardNotFoundException
   - etc.

3. Update GlobalExceptionHandler để handle:
   - Custom exceptions → proper HTTP codes
   - Database exceptions → 500
   - Validation exceptions → 400
   - Unknown exceptions → 500 with generic message

4. Viết 10+ tests

### Acceptance Criteria:
- ✅ Custom exceptions implemented
- ✅ All exceptions handled properly
- ✅ Error responses structured
- ✅ Error codes consistent
- ✅ Trace ID for debugging
- ✅ 10+ tests passing

### 💡 Tips for Fresher:
- **Don't implement circuit breaker** - too advanced
- Focus on custom exceptions + handler
- Each custom exception → specific HTTP code
- Use traceId: UUID.randomUUID().toString()
- Test different exception scenarios

### 🔍 What Interviewers Look For:
- Can you design custom exceptions?
- Do you understand HTTP status codes?
- Can you write centralized error handling?
- Are error messages helpful?

### ⚠️ MODIFICATIONS for Fresher:
- **Skip circuit breaker completely**
- **Focus on basics: custom exceptions + handler**

---

## 🎯 Ticket #11: Implement Caching Layer
**Tier**: ⭐⭐⭐ JUNIOR+
**Level**: Medium-Hard*
**Estimated Time**: 4-5 hours

### 🎯 Why This Ticket?
**Business Context**:
- Users hate slow APIs (every 100ms delay = 1% users leave)
- Database queries are slow (disk I/O is expensive)
- Same data requested multiple times (card summary, exchange rates)
- Caching = 10x-100x faster response times
- Cost savings (fewer DB queries = fewer servers needed)

**What You'll Learn**:
- ✅ Caching strategy (what to cache, how long)
- ✅ Cache invalidation (hardest problem in CS!)
- ✅ Cache hit rates (measure performance)
- ✅ Spring Cache abstraction (most teams use it)
- ✅ In-memory vs distributed caching

**Impact**:
- Dramatically faster API responses
- Better user experience
- Reduced server load
- Cost savings on infrastructure

### ⚠️ SIMPLIFIED for Fresher:
- Use **Caffeine in-memory cache ONLY** (not Redis)
- **Skip metrics/monitoring endpoint**
- Focus on basic @Cacheable/@CacheEvict

### Mô tả:
Add caching để improve performance, especially for frequently accessed data.

### Yêu cầu:
1. Integrate Spring Cache với:
   - @Cacheable, @CacheEvict, @CachePut annotations
   - **Use Caffeine cache** (in-memory, simpler than Redis)

2. Cache strategy cho:
   - Card summaries (invalidate after capture)
   - Transaction lists (invalidate after new transaction)

3. **SKIP cache metrics endpoint** - too much work

4. Viết 8+ tests:
   - Cache hit/miss behavior
   - Cache invalidation
   - Concurrency with caching

### Acceptance Criteria:
- ✅ Caching configured (Caffeine)
- ✅ @Cacheable/@CacheEvict working
- ✅ 8+ tests passing
- ✅ Performance improved
- ✅ Documentation updated

### 💡 Tips for Fresher:
- **Use Caffeine, not Redis** - simpler, in-memory
- @Cacheable: cache result of method
- @CacheEvict: clear cache when data changes
- Test cache with: @EnableCaching in test config
- Simple configuration: app.properties or @Configuration class

### 🔍 What Interviewers Look For:
- Do you understand Spring Cache abstraction?
- Can you design cache strategy?
- Do you handle cache invalidation?
- Are tests comprehensive?

### ⚠️ MODIFICATIONS for Fresher:
- **Caffeine only (not Redis)**
- **Skip metrics endpoint**
- **Focus on basic @Cacheable/@CacheEvict**

---

## 🎯 Ticket #12: Add Transaction Settlement with Delayed Processing
**Tier**: ⭐⭐⭐⭐ STRETCH GOAL
**Level**: Hard
**Estimated Time**: 6-7 hours

### 🎯 Why This Ticket?
**Business Context**:
- Real banking: transactions don't settle immediately
- Clearing & settlement takes time (T+2, T+3 in many industries)
- Settlement is batch process (combine many transactions, process once daily)
- Reconciliation needed (match our records with bank)
- Holds vs settled (hold amount now, settle amount later)

**What You'll Learn**:
- ✅ Batch processing patterns (process multiple items efficiently)
- ✅ Scheduled jobs (@Scheduled, cron expressions)
- ✅ State machines (PENDING → SETTLING → SETTLED)
- ✅ Async processing (don't block on slow operations)
- ✅ Report generation (batch results)

**Impact**:
- Matches real banking operations
- Better financial accuracy
- Reconciliation capability
- Professional-grade system

### ⚠️ SIMPLIFIED for Fresher:
- **Simplified settlement logic**
- Provide scheduled task template
- Skip complex report generation

### Mô tả:
Implement settlement process - transactions không capture immediately mà after certain period.

### Yêu cầu:
1. Add settlement fields vào CardTransaction:
   - settlementDate, settledAmount, settlementStatus

2. Add new transaction state: PENDING_SETTLEMENT

3. Create SettlementService:
   - Simple batch process for pending settlements
   - Update settled amounts
   - Handle partial settlements

4. Implement scheduled task:
   - Run daily để process settlements
   - Basic settlement logic

5. Create settlement API:
   - `GET /settlements` - list pending settlements
   - `POST /settlements/process` - trigger settlement (optional)

6. Viết 10+ tests:
   - Settlement processing
   - Basic edge cases

### Acceptance Criteria:
- ✅ Settlement logic implemented
   - Scheduled task chạy đúng
   - Failed settlement handling
   - 10+ tests passing

### 💡 Tips for Fresher:
- @Scheduled(cron = "0 0 2 * * *") = daily at 2am
- Simple settlement: mark as SETTLED, update amount
- Don't over-engineer - keep it simple
- Focus on core logic, not reports

### 🔍 What Interviewers Look For:
- Can you design settlement domain?
- Can you implement scheduled tasks?
- Do you handle failures?
- Are tests comprehensive?

### ⚠️ STRETCH GOAL
- Only attempt after completing Tier 1-2
- Advanced topic for filtering top 10%

---

## 🎯 Ticket #13: Add Transaction Fee Calculation
**Tier**: ⭐⭐⭐ JUNIOR+
**Level**: Medium
**Estimated Time**: 4-5 hours

### 🎯 Why This Ticket?
**Business Context**:
- Banks make money from fees (transaction fee, annual fee, etc)
- Different card types have different fees (student vs premium)
- Fees affect business model significantly
- Transparency required (show fees to customers)
- Accuracy critical (small % error = big money loss at scale)

**What You'll Learn**:
- ✅ Fee calculation logic (fixed + percentage)
- ✅ BigDecimal for money (precision matters!)
- ✅ Configuration management (change fees without code change)
- ✅ Business rule modeling (different cards, different rules)
- ✅ Testing monetary calculations (rounding errors!)

**Impact**:
- Revenue generation for business
- Transparent pricing for customers
- Flexible fee structure (easy to adjust)
- Professional money handling

### Mô tả:
Implement transaction fee calculation dựa trên amount hoặc card type.

### Yêu cầu:
1. Add CardType enum: STANDARD, PREMIUM, VIP

2. Add fee configuration:
   - Per-transaction fee (fixed + percentage)
   - Different fee structure per card type
   - Fee waiver conditions (min amount, etc)

3. Extend CardTransaction:
   - feeAmount, netAmount (amount - fee)

4. Update authorize endpoint:
   - Calculate fee automatically
   - Deduct fee from available limit
   - Include fee info in response

5. Create fee management API:
   - `GET /fees/config` - get current fee config
   - `POST /fees/config` - update fee config (admin)

6. Viết 10+ tests:
   - Fee calculation accuracy
   - Different card types
   - Fee waiver conditions
   - Edge cases

### Acceptance Criteria:
- ✅ Fee calculation correct
- ✅ Fees deducted properly
- ✅ Fee API working
- ✅ 10+ tests passing
- ✅ Configuration externalized

### 💡 Tips for Fresher:
- Fee calculation: amount * percentage + fixed amount
- BigDecimal for precise calculations
- Test various fee structures
- Edge case: negative amounts, zero fees

### 🔍 What Interviewers Look For:
- Can you design fee structure?
- Do you handle all card types?
- Is calculation accurate?
- Are edge cases covered?

---

## 🎯 Ticket #14: Implement API Documentation with Swagger/OpenAPI
**Tier**: ⭐⭐ JUNIOR ENTRY-LEVEL
**Level**: Easy
**Estimated Time**: 2-3 hours

### 🎯 Why This Ticket?
**Business Context**:
- APIs without docs = useless (how do people use it?)
- Manual docs get out of sync with code (bad!)
- Auto-generated docs = always accurate
- Swagger = industry standard (every company uses it)
- 10 minutes learning = 10 hours saved for API consumers

**What You'll Learn**:
- ✅ OpenAPI/Swagger standards
- ✅ Self-documenting APIs
- ✅ API contract definition
- ✅ Professional API development
- ✅ Testing APIs visually (Swagger UI)

**Impact**:
- APIs become usable immediately
- Fewer support questions
- Professional first impression
- Easy testing & debugging

### Mô tả:
Add Swagger/OpenAPI documentation cho tất cả endpoints.

### Yêu cầu:
1. Add SpringDoc OpenAPI dependency

2. Annotate tất cả controllers với:
   - @Operation
   - @ApiResponse
   - @Parameter
   - @RequestBody
   - @Schema


3. Configure Swagger UI:
   - Custom title, description
   - API version
   - Contact info

4. Document models với @Schema annotations

5. Test Swagger UI accessible at `/swagger-ui.html`

### Acceptance Criteria:
- ✅ Swagger UI accessible
- ✅ All endpoints documented
- ✅ Request/response examples
- ✅ Schema definitions clear
- ✅ Try-it-out feature working

### 💡 Tips for Fresher:
- Just add annotations - SpringDoc does most work
- @Operation: describe what endpoint does
- @ApiResponse: describe response status codes
- @Parameter: describe path/query parameters
- @Schema: describe DTO fields

### 🔍 What Interviewers Look For:
- Can you use Spring annotations?
- Do you document thoroughly?
- Are examples clear?
- Is API self-explanatory?

### ⚠️ EASY TICKET
- Good starting point for Tier 1
- No complex logic needed
- Quick win for confidence

---

## 🎯 Ticket #15: Database Performance Optimization
**Tier**: ⭐⭐⭐⭐ STRETCH GOAL
**Level**: Hard
**Estimated Time**: 5-6 hours

### 🎯 Why This Ticket?
**Business Context**:
- Slow database = slow application (10ms query = 10x slower app)
- At scale: 100ms slow query with million users = disaster
- N+1 problem: 1 query becomes 1000 queries (1000x slower!)
- Indexes: 100ms → 1ms (100x speedup!)
- Performance = money (fewer servers needed, better user experience)

**What You'll Learn**:
- ✅ Query analysis (find slow queries)
- ✅ N+1 problem detection (common expensive mistake)
- ✅ Query optimization (JOIN FETCH, projections)
- ✅ Index strategy (where to add indexes)
- ✅ Performance measurement (before/after)

**Impact**:
- 10x-100x faster database queries
- Better user experience (faster app)
- Cost savings (fewer servers)
- Scalability (handle more users)

### ⚠️ SIMPLIFIED for Fresher:
- Provide Hibernate stats setup template
- Focus on **N+1 problem detection & JOIN FETCH fix**
- Index creation template provided
- Simple performance testing

### Mô tả:
Identify và optimize database performance bottlenecks, improve query execution time.

### Yêu cầu:
1. **Analyze current queries**:
   - Enable Hibernate statistics logging
   - Identify N+1 query problem
   - Find slow queries using database logs
   - Basic query analysis

2. **Implement query optimizations**:
   - Add appropriate indexes (cardId, createdAt, type)
   - Use JOIN FETCH để avoid N+1 problems
   - Query projection (SELECT specific columns)

3. **Optimize entity relationships**:
   - Change lazy loading strategy nếu cần
   - Use @Query custom queries thay vì derived queries nếu better

4. **Add database indexes**:
   - Create indexes on cardId, createdAt, type
   - Provide migration template
   - Document index strategy

5. **Create performance test**:
   - Generate sample data (1k cards, 10k transactions - simplified)
   - Benchmark key operations
   - Measure improvement (before/after)

6. Viết 8+ tests:
   - Performance regression tests
   - Query optimization verification

### Acceptance Criteria:
- ✅ N+1 query problem fixed
- ✅ Indexes added and documented
- ✅ Query performance improved (30%+ faster)
- ✅ 8+ tests passing
- ✅ Performance report generated

### 💡 Tips for Fresher:
- **Hibernate stats**: enable in application.yml
- **N+1 problem**: use JOIN FETCH in @Query
- **Indexes**: add in migration, not JPA annotation
- **Simple test**: before/after query timing
- Don't over-engineer - focus on N+1 + indexes

### 🔍 What Interviewers Look For:
- Do you understand N+1 problem?
- Can you write efficient queries?
- Do you understand indexing?
- Can you measure performance?

### ⚠️ STRETCH GOAL
- Advanced database knowledge required
- Filter top candidates with database optimization skills

---

## 🎯 Ticket #16: Add Multi-Currency Support
**Tier**: ⭐⭐⭐⭐ STRETCH GOAL
**Level**: Hard*
**Estimated Time**: 6-7 hours

### 🎯 Why This Ticket?
**Business Context**:
- Global companies = multiple currencies
- Real users travel & use different currencies
- Exchange rates fluctuate (need to track accuracy)
- Regulatory requirement (handle foreign transactions)
- Conversion errors = money bugs (very expensive!)

**What You'll Learn**:
- ✅ Multi-currency architecture (design for scale)
- ✅ Exchange rate management (fetch, cache, update)
- ✅ BigDecimal precision (rounding modes for money)
- ✅ Currency conversion logic (accurate calculations)
- ✅ Edge case handling (zero rates, unusual pairs)

**Impact**:
- Enables global expansion
- Better user experience (support their currency)
- Accurate financial calculations
- Competitive advantage

### ⚠️ SIMPLIFIED for Fresher:
- **Mock exchange rates** (static file, not real API)
- Focus on **conversion logic, not rate fetching**
- Simplified currency support (3-4 currencies)

### Mô tả:
Extend system để support multiple currencies với exchange rate handling.

### Yêu cầu:
1. Add currency field vào CreditCard entity

2. Create ExchangeRate entity:
   - fromCurrency, toCurrency, rate

3. Create ExchangeRateService:
   - Mock rates (static, not API)
   - Convert amounts (BigDecimal math)

4. Update authorize endpoint:
   - Support multi-currency transactions
   - Auto-convert to card currency

5. Create currency API:
   - `GET /currencies` - list supported currencies
   - `GET /exchange-rates` - get current rates

6. Viết 10+ tests:
   - Currency conversion accuracy
   - Different currency pairs
   - Rounding & precision with BigDecimal
   - Edge cases

### Acceptance Criteria:
- ✅ Multi-currency conversion working
- ✅ Conversion accuracy (rounding correct)
- ✅ Currency API functional
- ✅ 10+ tests passing

### 💡 Tips for Fresher:
- **Use static exchange rates** (no API integration)
- BigDecimal for precision: `amount.multiply(rate)`
- Handle rounding: `setScale(2, RoundingMode.HALF_UP)`
- Test: USD to VND, VND to USD, etc
- Edge case: very small amounts, very large rates

### 🔍 What Interviewers Look For:
- Do you understand currency conversion?
- Can you handle BigDecimal correctly?
- Do you handle rounding properly?
- Are edge cases covered?

### ⚠️ MODIFICATIONS for Fresher:
- **Mock rates only** (no real API)
- **Static configuration** (no updates)
- **Focus on conversion logic**

---

## 📊 Scoring Rubric

| Criteria | Points | Details |
|----------|--------|---------|
| Functionality | 30 | Feature works as specified |
| Code Quality | 25 | Clean code, proper naming, DRY principle |
| Testing | 25 | Good test coverage, edge cases handled |
| Documentation | 10 | Comments, Javadoc, README updates |
| Git Practice | 10 | Good commit messages, PR description |
| **Total** | **100** | Pass >= 70 |

---

## 🎯 RECOMMENDED TICKET SELECTIONS

### For Fresher Entry-Level (Choose 1):
```
⭐ #14: Swagger/OpenAPI Documentation (EASY - 2-3h) - START HERE
⭐ #1:  Refund Transaction Feature (MEDIUM - 4-6h)
⭐ #3:  Pagination & Filtering (MEDIUM - 4-5h)
```

### For Junior+ with Experience (Choose 1):
```
⭐ #2:  Custom Validation Annotations (MEDIUM - 3-5h)
⭐ #13: Transaction Fee Calculation (MEDIUM - 4-5h)
⭐ #10: Error Handling & Recovery (MEDIUM - 4-5h)
```

### For Stretch Goals (Choose 1 optional):
```
⭐ #6:  Concurrent Transaction Handling (HARD - 5-7h)
⭐ #7:  Card Spending Limits (HARD - 5-6h)
⭐ #12: Transaction Settlement (HARD - 6-7h)
⭐ #15: Database Performance (HARD - 5-6h)
```

### Tickets with Required Simplifications:
```
⚠️ #4:  Audit Logging (HARD → MEDIUM-HARD): Use manual logging, not AOP
⚠️ #5:  Status History (HARD → MEDIUM-HARD): Use manual tracking, not lifecycle
⚠️ #8:  Database Migration (MEDIUM-HARD → MEDIUM): Use Flyway, provide template
⚠️ #9:  Rate Limiting (HARD → HARD): Use simple in-memory, not Redis
⚠️ #11: Caching Layer (MEDIUM-HARD → MEDIUM-HARD): Use Caffeine, not Redis
⚠️ #16: Multi-Currency (HARD → HARD*): Use mock rates, not real API
```

---

## 🚀 Expected Outcomes

Sau khi hoàn thành tickets, candidate nên:
- ✅ Hiểu rõ Spring Boot architecture
- ✅ Biết implement business logic
- ✅ Viết comprehensive tests
- ✅ Biết design API tốt
- ✅ Practice git workflow
- ✅ Understand practical trade-offs

---

## 💡 Tips for Test Administrators

1. **Tier 1 candidates**: Assign #14 (easy) hoặc #1, #3 (medium)
2. **Tier 2 candidates**: Assign #2, #13, #10 (medium-hard)
3. **Top candidates**: Offer stretch goals #6, #7, #12, #15 (hard)
4. **Time management**: 
   - Easy (2-3h) → full 4-6h time
   - Medium (4-5h) → full 4-6h time  
   - Hard (5-7h) → full 6-8h time
5. **Scoring**: Pass >= 70 points (40% pass = 3-5 from 100)

---

## 💡 Tips for Candidates

1. **Start with ticket #1-3** (medium) trước khi làm hard tickets
2. **Read the requirements carefully** - edge cases là cái bạn sẽ fail nếu not careful
3. **Write tests first** (TDD approach) hoặc at least test comprehensive
4. **Ask clarifying questions** nếu không hiểu requirement
5. **Code review yourself** trước khi submit PR
6. **Performance matters** - optimize queries, caching, etc
7. **Security matters** - validate inputs, prevent injection, etc

---

## ❓ Q&A Protocol

- Candidates có thể hỏi questions về requirements
- Nhưng KHÔNG được hỏi "how to implement"
- Encourage candidates tự research và problem-solve
- This is THE test - tính tự lực của họ

---

**Created**: February 5, 2026  
**For**: Technical Interview Assessment  
**Level**: Junior Developer (Expected)
