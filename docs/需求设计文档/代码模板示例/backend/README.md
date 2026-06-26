# Backend Skeleton

This folder contains a lightweight Java backend skeleton derived from the Study Library API documents.

Suggested structure highlights:
- `pom.xml`: Maven dependency baseline
- `src/main/resources/application.yml`: basic config placeholder
- `common/`: response, error, constant, exception skeletons
- `modules/`: auth, course, practice, content, member, system, user modules

How to use:
1. Copy this skeleton into a real backend repository.
2. Replace placeholder `Object` types with real DTO/entity/VO classes.
3. Add Spring annotations, persistence framework, security, and database config.
4. Align request/response models with `学习通OpenAPI草案.yaml`.

Suggested next steps:
- Replace placeholder classes with real Spring Boot annotations and entities
- Add persistence, security, and exception middleware
- Map OpenAPI schemas to DTO/VO classes
