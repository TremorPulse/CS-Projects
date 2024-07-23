import React from 'react';
import { Doughnut } from 'react-chartjs-2';
import 'chart.js/auto';

const DonutChart = ({ steps, stepGoal }) => {
  const stepsDone = steps > stepGoal ? stepGoal : steps; // Ensuring steps do not exceed the goal for visual purposes
  const stepsRemaining = stepGoal - stepsDone > 0 ? stepGoal - stepsDone : 0;

  const data = {
    labels: ['Steps Taken', 'Steps Remaining'],
    datasets: [{
      data: [stepsDone, stepsRemaining],
      backgroundColor: [
        'rgba(0, 128, 128, 0.6)', 
        'rgba(240,230,140, 0.6)' 
      ],
      borderColor: [
        'rgba(75, 192, 192, 1)',
        'rgba(255, 206, 86, 1)'
      ],
      borderWidth: 1
    }]
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      tooltip: {
        callbacks: {
          label: function(tooltipItem) {
            return `${tooltipItem.label}: ${tooltipItem.parsed} steps`;
          }
        }
      }
    },
    cutout: '50%',
  };

  return <Doughnut data={data} options={options} />;
}

export default DonutChart;
