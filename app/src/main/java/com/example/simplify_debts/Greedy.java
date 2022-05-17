package com.example.simplify_debts;

import android.content.Intent;

import androidx.core.content.res.TypedArrayUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class Greedy {
    static int people = 0; // number of people
    static int debts = 0; //how many debt
    static ArrayList<String> name_ = new ArrayList<>();
    static ArrayList<String> giver_ = new ArrayList<>();
    static ArrayList<String> taker_ = new ArrayList<>();
    static ArrayList<String> money_ = new ArrayList<>();
    //return index of minimum value in arr[]
    static int getMin(int arr[]) {
        int minInd = 0;
        for (int i = 1; i < people+1; i++) {
            if (arr[i] < arr[minInd]) {
                minInd = i;
            }
        }
        return minInd;
    }

    static int getMax(int arr[]) {
        int maxInd = 0;
        for (int i = 1; i < people+1; i++) {
            if (arr[i] > arr[maxInd]) {
                maxInd = i;
            }
        }
        return maxInd;
    }


    static int minOf2(int x, int y) {
        return (x < y) ? x : y;
    }

    static void cashFlowRec(int[] amount) {
        int mxCredit = getMax(amount), mxDebt = getMin(amount);
        if (amount[mxCredit] == 0 && amount[mxDebt] == 0)
            return;
        int min = minOf2(-amount[mxDebt], amount[mxCredit]);
        amount[mxCredit] -= min;
        amount[mxDebt] +=min;

        System.out.println("Person " + mxDebt + " pays " + min
                + " to " + "Person " + mxCredit);

        giver_.add(name_.get(mxDebt));
        taker_.add(name_.get(mxCredit));
        money_.add(Integer.toString(min));
        cashFlowRec(amount);
    }

    static void minimizeCashFlow(int[][] graph){
        // amount[p] indicates the net amount
        int amount[] = new int[people+1];
        for(int i = 0; i<debts;i++){
            int giver = graph[i][0]; int taker = graph[i][1];
            amount[taker] += graph[i][2];
            amount[giver] -= graph[i][2];

        }

        cashFlowRec(amount);
    }

    static Intent main(ArrayList<String> name, ArrayList<Integer> giver, ArrayList<Integer>taker, ArrayList<Integer> money){
        giver_.clear();
        taker_.clear();
        money_.clear();
        name_ = name;
        int [][] graph = new int[giver.size()][3];
        debts = giver.size();
        for(int i =0;i<giver.size();i++){
                graph[i][0] = giver.get(i);
                graph[i][1] = taker.get(i);
                graph[i][2] = money.get(i);
                if(people<giver.get(i))people = giver.get(i);
                if(people<taker.get(i))people = taker.get(i);
        }
        minimizeCashFlow(graph);

        Intent intent = new Intent();
        intent.putStringArrayListExtra("giver",giver_);
        intent.putStringArrayListExtra("money",money_);
        intent.putStringArrayListExtra("taker",taker_);
        return intent;
    }

}
