package com.cct.onlineteaching.controller;

import com.cct.onlineteaching.entity.Evaluation;
import com.cct.onlineteaching.service.EvaluationService;
import com.cct.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class EvaluationController {
    private EvaluationService evaluationService;

    @Autowired
    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/evaluation")
    public Map<String, Object> createEvaluation(@RequestParam("uid") int userId,
                                                @RequestBody Evaluation evaluation) {
        evaluation.setUserId(userId);
        evaluationService.createEvaluation(evaluation);

        return ResponseUtil.success(null);
    }

    @GetMapping("/evaluation/list")
    public Map<String, Object> getEvaluationList(@RequestParam("courseId") int courseId) {
        Map<String, Object> evaluationList = evaluationService.getEvaluationList(courseId);
        return ResponseUtil.success(evaluationList);
    }
}
